package sysnetlab.android.sdc.datastore;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;
import sysnetlab.android.sdc.datacollector.DateUtils;
import sysnetlab.android.sdc.datacollector.DeviceInformation;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.ExperimentTime;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.datacollector.TaggingAction;
import sysnetlab.android.sdc.datacollector.TaggingState;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorUtilSingleton;

public class SimpleXMLFileStore extends SimpleFileStore {

    private final String XMLNS = "http://schemas.sysnetlab.net/apps/sdc";
    private final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    private final String XSI_SCHEMA_LOCATION = "http://schemas.sysnetlab.net/apps/sdc ExperimentMetaData.xsd";
    private final String XML_EXPERIMENT_META_DATA_FILE = ".experiment.xml";

    public SimpleXMLFileStore() throws RuntimeException {
    }

    @Override
    public List<Experiment> listStoredExperiments() {
        return super.listStoredExperiments();
    }

    @Override
    public void writeExperimentMetaData(Experiment experiment) {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        
        try {
            StringWriter stringWriter = new StringWriter();
            xmlSerializer.setOutput(stringWriter);
            
            xmlSerializer.startDocument("UTF-8", false);
            xmlSerializer.startTag("", "experiment");
            xmlSerializer.attribute("", "xmlns", XMLNS);
            xmlSerializer.attribute("", "xmlns:xsi", XMLNS_XSI);
            xmlSerializer.attribute("", "xsi:schemaLocation", XSI_SCHEMA_LOCATION);

            // <name> ... </name>
            xmlSerializer.startTag("", "name");
            xmlSerializer.text(experiment.getName());
            xmlSerializer.endTag("", "name");

            // <device> ... </device>
            xmlSerializer.startTag("", "device");
            xmlSerializer.startTag("", "android");

            xmlSerializer.startTag("", "make");
            xmlSerializer.text(experiment.getDeviceInformation().getManufacturer());
            xmlSerializer.endTag("", "make");

            xmlSerializer.startTag("", "model");
            xmlSerializer.text(experiment.getDeviceInformation().getModel());
            xmlSerializer.endTag("", "model");

            xmlSerializer.endTag("", "android");
            xmlSerializer.endTag("", "device");

            // <datetimecreation> ... </datetimecreation>
            xmlSerializer.startTag("", "datetimecreation");
            xmlSerializer.text(experiment.getDateTimeCreatedAsStringUTC());
            xmlSerializer.endTag("", "datetimecreation");

            // <datetimecompletion> ... </datetimecompletion>
            xmlSerializer.startTag("", "datetimecompletion");
            xmlSerializer.text(experiment.getDateTimeCreatedAsStringUTC());
            xmlSerializer.endTag("", "datetimecompletion");

            // <taglist> ... </taglist>
            if (!experiment.getTags().isEmpty()) {
                xmlSerializer.startTag("", "taglist");

                for (Tag tag : experiment.getTags()) {
                    xmlSerializer.startTag("", "tag");

                    xmlSerializer.startTag("", "name");
                    xmlSerializer.text(tag.getName());
                    xmlSerializer.endTag("", "name");

                    if (tag.getShortDescription().trim().length() > 0) {
                        xmlSerializer.startTag("", "shortdescription");
                        xmlSerializer.text(tag.getShortDescription());
                        xmlSerializer.endTag("", "shortdescription");
                    }

                    if (tag.getLongDescription().trim().length() > 0) {
                        xmlSerializer.startTag("", "longdescription");
                        xmlSerializer.text(tag.getLongDescription());
                        xmlSerializer.endTag("", "longdescription");
                    }

                    xmlSerializer.endTag("", "tag");
                }

                xmlSerializer.endTag("", "taglist");
            }

            // <notelist> ... </notelist>
            if (!experiment.getNotes().isEmpty()) {
                xmlSerializer.startTag("", "notelist");

                for (Note note : experiment.getNotes()) {
                    xmlSerializer.startTag("", "note");

                    xmlSerializer.startTag("", "notetext");
                    xmlSerializer.text(note.getNote());
                    xmlSerializer.endTag("", "noteteext");

                    xmlSerializer.startTag("", "datetime");
                    xmlSerializer.text(note.getDateCreatedAsStringUTC());
                    xmlSerializer.endTag("", "datetime");

                    xmlSerializer.endTag("", "note");
                }

                xmlSerializer.endTag("", "notelist");
            }

            // <taggingactionlist> ... </taggingactionlist>
            if (!experiment.getTaggingActions().isEmpty()) {
                xmlSerializer.startTag("", "taggingactionlist");

                for (TaggingAction action : experiment.getTaggingActions()) {
                    xmlSerializer.startTag("", "tagreference");
                    xmlSerializer.attribute("", "name", action.getTag().getName());
                    xmlSerializer.endTag("", "tagreference");

                    xmlSerializer.startTag("", "experimenttime");

                    xmlSerializer.startTag("", "threadtimemillis");
                    xmlSerializer.text(Long.toString(action.getTime().getThreadTimeMillis()));
                    xmlSerializer.endTag("", "threadtimemillis");

                    xmlSerializer.startTag("", "elapsedrealtime");
                    xmlSerializer.text(Long.toString(action.getTime().getElapsedRealtime()));
                    xmlSerializer.endTag("", "elapsedrealtime");

                    if (action.getTime().getElapsedRealtimeNanos() >= 0) {
                        xmlSerializer.startTag("", "elapsedrealtimenanos");
                        xmlSerializer.text(Long
                                .toString(action.getTime().getElapsedRealtimeNanos()));
                        xmlSerializer.endTag("", "elapsedrealtimenanos");
                    }

                    xmlSerializer.endTag("", "experimenttime");

                    xmlSerializer.startTag("", "taggingstate");
                    xmlSerializer.startTag("", "name");
                    xmlSerializer.text(action.getTagState().toString());
                    xmlSerializer.endTag("", "name");
                    xmlSerializer.endTag("", "taggingstate");
                }

                xmlSerializer.endTag("", "taggingactionlist");
            }

            // <sensorlist> ... </sensorlist>
            if (!experiment.getSensors().isEmpty()) {
                xmlSerializer.startTag("",  "sensorlist");
                
                int id = 0;
                for (AbstractSensor sensor : experiment.getSensors()) {
                
                xmlSerializer.startTag("",  "sensor");
                xmlSerializer.attribute("", "id", Integer.toString(id));
                xmlSerializer.startTag("",  "name");
                xmlSerializer.text(sensor.getName());
                xmlSerializer.endTag("",  "name");
                
                xmlSerializer.startTag("", "majortype");
                xmlSerializer.text(Integer.toString(sensor.getMajorType()));
                xmlSerializer.endTag("", "majortype");
                
                xmlSerializer.startTag("", "minortype");
                xmlSerializer.text(Integer.toString(sensor.getMinorType()));
                xmlSerializer.endTag("", "minortype");
                
                xmlSerializer.endTag("",  "sensor");
                }
                
                xmlSerializer.endTag("",  "sensorlist");
            }

            xmlSerializer.endTag("", "experiment");

            xmlSerializer.endDocument();

            String configFilePath = getNewExperimentPath() + "/" + XML_EXPERIMENT_META_DATA_FILE;
            PrintStream out;
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream(configFilePath)));
            
            out.println(stringWriter.toString());
            out.close();
            Log.d("SensorDataCollector.UnitTest", "xml file = <" + stringWriter.toString() + ">");
        } catch (IllegalArgumentException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector.UnitTest", e.toString());
            
        } catch (IllegalStateException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector.UnitTest", e.toString());            
        } catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector.UnitTest", e.toString());            
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector.UnitTest", e.toString());            
        } 
    }
    
    public Experiment loadExperiment(String experimentPath) {
        Experiment experiment = null;
        
        String configFilePath = experimentPath + "/" + XML_EXPERIMENT_META_DATA_FILE;
        
        try {
            
            BufferedReader in = new BufferedReader(new FileReader(configFilePath));
        
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            
            xpp.setInput(in);
            
            xpp.nextTag();
            experiment = readExperiment(xpp);
        
            in.close();
        } catch (FileNotFoundException e) {
            experiment = null;
            Log.d("SensorDataCollector.UnitTest", "not found " + configFilePath);
        } catch (XmlPullParserException e) {
            experiment = null;
            Log.d("SensorDataCollector.UnitTest", e.toString());            
        } catch (IOException e) {
            experiment = null;
            Log.d("SensorDataCollector.UnitTest", e.toString());            
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return experiment;
    }
    
    private Experiment readExperiment(XmlPullParser xpp) throws IllegalStateException,
            XmlPullParserException, IOException, ParseException {
        Experiment experiment = new Experiment();

        xpp.require(XmlPullParser.START_TAG, XMLNS, "experiment");

        while (xpp.next() != XmlPullParser.END_DOCUMENT) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elem = xpp.getName();

            Log.d("SensorDataCollector.UnitTest", "element = " + elem);

            if (elem.equals("name")) {
                if (xpp.next() != XmlPullParser.TEXT) {
                    throw new IllegalStateException("SimpleXMLFileStore::readExperiment(): ill-formed XML for experiment name");
                }
                experiment.setName(xpp.getText());
            } else if (elem.equals("device")) {
                DeviceInformation device = readDevice(xpp);
                experiment.setDeviceInformation(device);
            } else if (elem.equals("datetimecreation")) {
                if (xpp.next() != XmlPullParser.TEXT) {
                    throw new IllegalStateException("SimpleXMLFileStore::readExperiment(): ill-formed XML for experiment datetimecreation");                    
                }
                experiment.setDateTimeCreatedFromStringUTC(xpp.getText());
            } else if (elem.equals("datetimecompletion")) {
                if (xpp.next() != XmlPullParser.TEXT) {
                    throw new IllegalStateException("SimpleXMLFileStore::readExperiment(): ill-formed XML for experiment datetimecompletion");                    
                }
                experiment.setDateTimeDoneFromStringUTC(xpp.getText());
            } else if (elem.equals("taglist")) {
                List<Tag> listTags = readTags(xpp);
                experiment.setTags(listTags);                
            } else if (elem.equals("notelist")) {
                List<Note> listNotes = readNotes(xpp);
                experiment.setNotes(listNotes);
            } else if (elem.equals("taggingactionlist")) {
                List<TaggingAction> listTaggingActions = readTaggingActions(xpp, experiment.getTags());
                experiment.setTaggingActions(listTaggingActions);
            } else if (elem.equals("sensorlist")) {
                List<AbstractSensor> listSensors = readSensors(xpp);
                experiment.setSensors(listSensors);
            }
        }
        
        String dbgString = " name = " + experiment.getName() +  
                " datetimecreated = " + experiment.getDateTimeCreatedAsStringUTC() +
                " datetimecompleted = " + experiment.getDateTimeDoneAsStringUTC();
        for (Tag tag : experiment.getTags()) {
            dbgString += " Tag = (" + tag.getName() + ", " + tag.getShortDescription() + ", " + tag.getLongDescription() + ") ";
        }

        Log.d("SensorDataCollector.UnitTest", dbgString);
                
        return experiment;
    }
    
    private DeviceInformation readDevice(XmlPullParser xpp) throws XmlPullParserException, IllegalStateException, IOException {
        DeviceInformation device = new DeviceInformation();

        xpp.require(XmlPullParser.START_TAG, XMLNS, "device");
        
        int depth = 1;
        String elem = "";
        boolean isAndroid = false;
        while (depth != 0) {          
            switch (xpp.next()) {
            case XmlPullParser.END_TAG:              
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                if (depth == 2 && xpp.getName().equals("android")) {
                    isAndroid = true;
                } else {
                    isAndroid = false;
                }
                elem = xpp.getName();
                break;
            case XmlPullParser.TEXT:
                Log.d("SensorDataCollector.UnitTest", "Depth = " + depth + " Element Start Tag = " + xpp.getName());
                // TODO only android is supported at present
                if (isAndroid && depth == 3) {
                    String elemText = xpp.getText();
                    Log.d("SensorDataCollector.UnitTest", "Depth = " + depth + " Element Text = " + elemText);
                    
                    if (elem.equals("make")) {
                        device.setManufacturer(elemText);
                    } else if (elem.equals("model")) {
                        device.setModel(elemText);
                    } else if (elem.equals("sdk-int")) {
                        if (elem != null) {
                            device.setSdkInt(Integer.parseInt(elemText));
                        }
                    } else if (elem.equals("sdk-codename")) {
                        device.setSdkCodeName(elemText);
                    }
                }                  
                break;                
            }
        }
        
        Log.d("SensorDataCollector.UnitTest", "device = " + "Make: " + device.getManufacturer()
                + " Model: " + device.getModel() + " SDK-INT: " + device.getSdkCodeName()
                + " SDK-CodeName: " + device.getSdkInt());
        
        return device;
    }
    
    private List<Tag> readTags(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<Tag> listTags = new ArrayList<Tag>();
            
        xpp.require(XmlPullParser.START_TAG, XMLNS, "taglist");

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("tag")) {
                listTags.add(readTag(xpp));
            } 
        }  

        return listTags;
    }
    
    private Tag readTag(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "tag");
        
        String tagName = null;
        String shortDescription = null;
        String longDescription = null;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("name")) {
                tagName = readTagName(xpp);
            } else if (name.equals("shortdescription")) {
                shortDescription = readShortDescription(xpp);
            } else if (name.equals("longdescription")) {
                longDescription = readLongDescription(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return new Tag(tagName, shortDescription, longDescription);
    }
    
    private String readTagName(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "name");
    }
    
    private String readShortDescription(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "shortdescription");
    }
    
    private String readLongDescription(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "longdescription");
    }
    
    private List<Note> readNotes(XmlPullParser xpp) throws XmlPullParserException, IOException, ParseException {
        List<Note> listNotes = new ArrayList<Note>();
        
        xpp.require(XmlPullParser.START_TAG, XMLNS, "notelist");

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("note")) {
                listNotes.add(readNote(xpp));
            } 
        }  

        return listNotes;
    }
    
    private Note readNote(XmlPullParser xpp) throws XmlPullParserException, IOException, ParseException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "note");
        
        String text = null;
        String dateTime = null;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("notetext")) {
                text = readNoteText(xpp);
            } else if (name.equals("datetime")) {
                dateTime = readNoteDateTime(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return new Note(text, DateUtils.getDatefromStringUTC(dateTime));
    }
    
    private String readNoteText(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "notetext");
    }
    
    private String readNoteDateTime(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "datetime");
    }
    
    private List<TaggingAction> readTaggingActions(XmlPullParser xpp, List<Tag> listTags) throws XmlPullParserException, IOException {
        List<TaggingAction> listTaggingActions = new ArrayList<TaggingAction>();
        
        xpp.require(XmlPullParser.START_TAG, XMLNS, "taggingactionlist");

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("taggingaction")) {
                listTaggingActions.add(readTaggingAction(xpp, listTags));
            } 
        }  

        return listTaggingActions;
    }
    
    private TaggingAction readTaggingAction(XmlPullParser xpp, List<Tag> listTags) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "taggingaction");
        
        Tag taggingTag = null;
        ExperimentTime taggingTime = null;
        TaggingState taggingState = TaggingState.TAG_CONTEXT;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("tagreference")) {
                String tagReference = xpp.getAttributeValue(XMLNS, "name"); 
                taggingTag = null;
                for (Tag tag : listTags) {
                    if (tag.getName().equals(tagReference)) {
                        taggingTag = tag;
                        break;
                    }
                }
                if (taggingTag == null) {
                    taggingTag = new Tag(tagReference);
                }
            } else if (name.equals("experimenttime")) {
                taggingTime = readTaggingActionTime(xpp);
            } else if (name.equals("taggingstate")) {
                taggingState = readTaggingState(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return new TaggingAction(taggingTag, taggingTime, taggingState);        
    }
    
    private ExperimentTime readTaggingActionTime(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "experimenttime");
        
        Long threadTimeMillis = -1l;
        Long elapsedRealTime = -1l;
        Long elapsedRealTimeNanos = -1l;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("threadtimemillis")) {
                threadTimeMillis = readThreadTimeMillis(xpp);
            } else if (name.equals("elapsedrealtime")) {
                elapsedRealTime = readElapsedRealTime(xpp);
            } else if (name.equals("elapsedrealtimenanos")) {
                elapsedRealTimeNanos = readElapsedRealTimeNanos(xpp);    
            } else {
                skip(xpp);
            }
        }
        
        return new ExperimentTime(threadTimeMillis, elapsedRealTime, elapsedRealTimeNanos);        
    }
    
    private Long readThreadTimeMillis(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String text = readXmlElementText(xpp, "threadtimemillis");
        return Long.parseLong(text);
    }
    
    private Long readElapsedRealTime(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String text = readXmlElementText(xpp, "elapsedrealtime");
        return Long.parseLong(text);
    }
    
    private Long readElapsedRealTimeNanos(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String text = readXmlElementText(xpp, "elapsedrealtimenanos");
        return Long.parseLong(text);
    }
    
    private TaggingState readTaggingState(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "taggingstate");
        
        TaggingState taggingState = TaggingState.TAG_CONTEXT;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("name")) {
                String taggingStateTextValue = readTaggingStateTextValue(xpp);   
                taggingState = TaggingState.valueOf(taggingStateTextValue);
            } else {
                skip(xpp);
            }
        }    
        
        return taggingState;
    }
    
    private String readTaggingStateTextValue(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "name");        
    }
    
    private List<AbstractSensor> readSensors(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<AbstractSensor> listSensors = new ArrayList<AbstractSensor>();
        
        xpp.require(XmlPullParser.START_TAG, XMLNS, "sensorlist");

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("sensor")) {
                listSensors.add(readSensor(xpp));
            } 
        }  

        return listSensors;
    }
    
    private AbstractSensor readSensor(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "sensor");
        
        int id = Integer.parseInt(xpp.getAttributeValue(XMLNS, "id"));
        
        String sensorName = null;
        int majorType = -1;
        int minorType = -1;

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("name")) {
                sensorName = readSensorName(xpp);
            } else if (name.equals("majortype")) {
                majorType = readSensorMajorType(xpp);
            } else if (name.equals("minortype")) {
                minorType = readSensorMinorType(xpp);
            }
        } 
        
        AbstractSensor sensor = SensorUtilSingleton.getInstance().getSensor(sensorName, majorType, minorType, id, null);
        return sensor;
    }
    
    private String readSensorName(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "name");
    }
    
    private int readSensorMajorType(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return Integer.parseInt(readXmlElementText(xpp, "majortype"));
    }
    
    private int readSensorMinorType(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return Integer.parseInt(readXmlElementText(xpp, "minortype"));
    }
    
    
    private String readXmlElementText(XmlPullParser xpp, String xmlElementName) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, xmlElementName);
        String text = readText(xpp);
        xpp.require(XmlPullParser.END_TAG, XMLNS, xmlElementName);
        return text;
    }
    
    private String readText(XmlPullParser xpp) throws IOException, XmlPullParserException {
        String text = "";
        if (xpp.next() == XmlPullParser.TEXT) {
            text = xpp.getText();
            xpp.nextTag();
        }
        return text;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }    

    @Override
    public Channel createChannel(String tag) {
        // TODO Auto-generated method stub
        return super.createChannel(tag);
    }

    @Override
    public void closeAllChannels() {
        // TODO Auto-generated method stub
        super.closeAllChannels();
    }


    @Override
    public int getNextChannelNumber() {
        // TODO Auto-generated method stub
        return super.getNextChannelNumber();
    }
    


}
