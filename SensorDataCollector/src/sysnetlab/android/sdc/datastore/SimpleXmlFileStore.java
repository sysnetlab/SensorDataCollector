package sysnetlab.android.sdc.datastore;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.hardware.Sensor;
import android.os.Build;
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
import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.SensorUtilsSingleton;

public class SimpleXmlFileStore extends SimpleFileStore {

    private final String XMLNS = "http://schemas.sysnetlab.net/apps/sdc";
    private final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    private final String XSI_SCHEMA_LOCATION = "http://schemas.sysnetlab.net/apps/sdc ExperimentMetaData.xsd";
    private final String XML_EXPERIMENT_META_DATA_FILE = "experiment.xml";

    public SimpleXmlFileStore() throws RuntimeException {
        super();
    }

    @Override
    public List<Experiment> listStoredExperiments() {
        List<Experiment> listExperiments = new ArrayList<Experiment>();

        DecimalFormat f = new DecimalFormat("00000");
        for (int i = 1; i < mNextExperimentNumber; i++) {
            String dirName = DIR_PREFIX + f.format(i);
            String pathPrefix = mParentPath + "/" + dirName;

            Experiment experiment; 
            
            String xmlConfigFile = pathPrefix + "/" + XML_EXPERIMENT_META_DATA_FILE;
            File configFile = new File(xmlConfigFile);
            
            if (!configFile.exists()) {
                experiment = super.loadExperiment(dirName, pathPrefix);
            } else {
                experiment = loadExperiment(pathPrefix);
            }
            if (experiment != null) {
                listExperiments.add(experiment);
            }
        }
        return listExperiments;
    }

    @Override
    public void writeExperimentMetaData(Experiment experiment) {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        
        try {
            StringWriter stringWriter = new StringWriter();
            xmlSerializer.setOutput(stringWriter);
            
            serializeExperiment(xmlSerializer, experiment); 
            
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
            experiment.setPath(experimentPath);
        
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

    
    private void serializeExperiment(XmlSerializer xs, Experiment e)
            throws IllegalArgumentException, IllegalStateException, IOException {
        xs.startDocument("UTF-8", false);
        xs.startTag("", "experiment");
        xs.attribute("", "xmlns", XMLNS);
        xs.attribute("", "xmlns:xsi", XMLNS_XSI);
        xs.attribute("", "xsi:schemaLocation", XSI_SCHEMA_LOCATION);

        // <name> ... </name>
        serializeExperimentNameElement(xs, e.getName());

        // <device> ... </device>
        serializeExperimentDeviceInfo(xs, e.getDeviceInformation());

        // <date time creation></date time creation><date time done></date time
        // done>
        serializeExperimentTimes(xs, e.getDateTimeCreated(), e.getDateTimeDone());

        // <tag list> ... </tag list>
        serializeExperimentTagList(xs, e.getTags());

        // <note list> ... </note list>
        serializeExperimentNoteList(xs, e.getNotes());

        // <tagging action list> ... </tagging action list>
        serializeExperimentTaggingActionList(xs, e.getTaggingActions());

        // <sensor list> ... </sensor list>
        serializeExperimentSensorList(xs, e.getSensors());

        xs.endTag("", "experiment");

        xs.endDocument();
    }
    
    private void serializeExperimentNameElement(XmlSerializer xs, String name)
            throws IllegalArgumentException, IllegalStateException, IOException {
        xs.startTag("", "name");
        xs.text(name);
        xs.endTag("", "name");
    }
    
    private void serializeExperimentDeviceInfo(XmlSerializer xs, DeviceInformation deviceInfo)
            throws IllegalArgumentException, IllegalStateException, IOException {
        xs.startTag("", "device");
        xs.startTag("", "android");

        xs.startTag("", "make");
        xs.text(deviceInfo.getManufacturer());
        xs.endTag("", "make");

        xs.startTag("", "model");
        xs.text(deviceInfo.getModel());
        xs.endTag("", "model");
        
        xs.startTag("", "sdk-int");
        xs.text(Integer.toString(deviceInfo.getSdkInt()));
        xs.endTag("", "sdk-int");
        
        xs.startTag("", "sdk-codename");
        xs.text(deviceInfo.getSdkCodeName());
        xs.endTag("",  "sdk-codename");

        xs.endTag("", "android");
        xs.endTag("", "device");
    }
    
    private void serializeExperimentTimes(XmlSerializer xs, Date dateCreated,
            Date dateDone) throws IllegalArgumentException, IllegalStateException, IOException {
        xs.startTag("", "datetimecreation");
        xs.text(DateUtils.getStringUTCFromDate(dateCreated));
        xs.endTag("", "datetimecreation");

        xs.startTag("", "datetimecompletion");
        xs.text(DateUtils.getStringUTCFromDate(dateDone));
        xs.endTag("", "datetimecompletion");
    }
    
    private void serializeExperimentTagList(XmlSerializer xs, List<Tag> listTags)
            throws IllegalArgumentException, IllegalStateException, IOException {
        if (!listTags.isEmpty()) {
            xs.startTag("", "taglist");

            for (Tag tag : listTags) {
                xs.startTag("", "tag");

                xs.startTag("", "name");
                xs.text(tag.getName());
                xs.endTag("", "name");

                if (tag.getShortDescription().trim().length() > 0) {
                    xs.startTag("", "shortdescription");
                    xs.text(tag.getShortDescription());
                    xs.endTag("", "shortdescription");
                }

                if (tag.getLongDescription().trim().length() > 0) {
                    xs.startTag("", "longdescription");
                    xs.text(tag.getLongDescription());
                    xs.endTag("", "longdescription");
                }

                xs.endTag("", "tag");
            }

            xs.endTag("", "taglist");
        }
    }
    
    private void serializeExperimentNoteList(XmlSerializer xs, List<Note> listNotes)
            throws IllegalArgumentException, IllegalStateException, IOException {
        if (!listNotes.isEmpty()) {
            xs.startTag("", "notelist");

            for (Note note : listNotes) {
                xs.startTag("", "note");

                xs.startTag("", "notetext");
                xs.text(note.getNote());
                xs.endTag("", "notetext");

                xs.startTag("", "datetime");
                xs.text(note.getDateCreatedAsStringUTC());
                xs.endTag("", "datetime");

                xs.endTag("", "note");
            }

            xs.endTag("", "notelist");
        }
    }
    
    private void serializeExperimentTaggingActionList(XmlSerializer xs,
            List<TaggingAction> listTaggingActions) throws IllegalArgumentException,
            IllegalStateException, IOException {
        if (!listTaggingActions.isEmpty()) {
            xs.startTag("", "taggingactionlist");

            for (TaggingAction action : listTaggingActions) {
                xs.startTag("", "tagreference");
                xs.attribute("", "name", action.getTag().getName());
                xs.endTag("", "tagreference");

                xs.startTag("", "experimenttime");

                xs.startTag("", "threadtimemillis");
                xs.text(Long.toString(action.getTime().getThreadTimeMillis()));
                xs.endTag("", "threadtimemillis");

                xs.startTag("", "elapsedrealtime");
                xs.text(Long.toString(action.getTime().getElapsedRealtime()));
                xs.endTag("", "elapsedrealtime");

                if (action.getTime().getElapsedRealtimeNanos() >= 0) {
                    xs.startTag("", "elapsedrealtimenanos");
                    xs.text(Long
                            .toString(action.getTime().getElapsedRealtimeNanos()));
                    xs.endTag("", "elapsedrealtimenanos");
                }

                xs.endTag("", "experimenttime");

                xs.startTag("", "taggingstate");
                xs.startTag("", "name");
                xs.text(action.getTagState().toString());
                xs.endTag("", "name");
                xs.endTag("", "taggingstate");
            }

            xs.endTag("", "taggingactionlist");
        }
    }
    
    private void serializeExperimentSensorList(XmlSerializer xs, List<AbstractSensor> listSensors)
            throws IllegalArgumentException, IllegalStateException, IOException {
        if (!listSensors.isEmpty()) {
            xs.startTag("", "sensorlist");

            for (AbstractSensor sensor : listSensors) {

                xs.startTag("", "sensor");
                xs.attribute("", "id", Integer.toString(sensor.getId()));
                xs.startTag("", "name");
                xs.text(sensor.getName());
                xs.endTag("", "name");

                xs.startTag("", "majortype");
                xs.text(Integer.toString(sensor.getMajorType()));
                xs.endTag("", "majortype");

                xs.startTag("", "minortype");
                xs.text(Integer.toString(sensor.getMinorType()));
                xs.endTag("", "minortype");
                
                xs.startTag("",  "selected");
                xs.text(Boolean.toString(sensor.isSelected()));
                xs.endTag("", "selected");

                switch(sensor.getMajorType()) {
                    case AbstractSensor.ANDROID_SENSOR:
                        serializeAndroidSensor(xs, (AndroidSensor) sensor);
                        break;
                }

                xs.endTag("", "sensor");
            }

            xs.endTag("", "sensorlist");
        }
    }
    
    @SuppressWarnings("deprecation")
    private void serializeAndroidSensor(XmlSerializer xs, AndroidSensor sensor) throws IllegalArgumentException, IllegalStateException, IOException {
        xs.startTag("",  "datadescription");
        xs.attribute("",  "type",  "csv");

        xs.startTag("",  "location");
        xs.text(sensor.getChannel().describe());
        xs.endTag("",  "location");

        final String eventTimes[] = {"threadtimemillis", 
                "elapsedrealtime",
                "elapsedrealtimenanos"};
        int pos = -1;
        for (int i = 0; i < eventTimes.length; i ++) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 && i == eventTimes.length - 1) {
                break;
            }
            
            pos ++;
            xs.startTag("", "column");
            xs.attribute("",  "id",  Integer.toString(pos));
            xs.attribute("",  "pos",  Integer.toString(pos));
            xs.attribute("",  "type",  "long");
            xs.attribute("", "description", eventTimes[i]);
            xs.endTag("", "column");
        }
        // http://developer.android.com/reference/android/hardware/SensorEvent.html#values
        int nValues;
        switch (sensor.getMinorType()) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_GYROSCOPE:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_LINEAR_ACCELERATION:
            case Sensor.TYPE_ORIENTATION:
                nValues = 3;
                break;

            case Sensor.TYPE_LIGHT:
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_PROXIMITY:
            case Sensor.TYPE_RELATIVE_HUMIDITY:
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                nValues = 1;
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                nValues = 3;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    nValues = 5;
                }
                break;
                
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                nValues = 6;
                break;
                
            default:
                nValues = 3;
        }
        for (int i = 0; i < nValues; i ++) {
            pos ++;
            xs.startTag("", "column");
            xs.attribute("",  "id",  Integer.toString(pos));
            xs.attribute("",  "pos",  Integer.toString(pos));
            xs.attribute("",  "type",  "double");
            xs.attribute("", "description", "value");
            xs.endTag("", "column");                    
        }  
        
        xs.endTag("",  "datadescription");        
    }
    
    private Experiment readExperiment(XmlPullParser xpp) throws IllegalStateException,
            XmlPullParserException, IOException, ParseException {
        Experiment experiment = new Experiment();

        xpp.require(XmlPullParser.START_TAG, XMLNS, "experiment");

        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String elem = xpp.getName();

            Log.d("SensorDataCollector.UnitTest", "element = " + elem);

            if (elem.equals("name")) {
                String name = readXmlElementText(xpp, "name");
                experiment.setName(name);
                Log.d("SensorDataCollector.UnitTest", "readExperiment(): " + xpp.getText());                
            } else if (elem.equals("device")) {
                DeviceInformation device = readDevice(xpp);
                experiment.setDeviceInformation(device);
                Log.d("SensorDataCollector.UnitTest", "readExperiment(): " + device.toString());                
            } else if (elem.equals("datetimecreation")) {
                String datetime = readXmlElementText(xpp, "datetimecreation");
                experiment.setDateTimeCreatedFromStringUTC(datetime);
                Log.d("SensorDataCollector.UnitTest", "readExperiment(): " + xpp.getText());
            } else if (elem.equals("datetimecompletion")) {
                String datetime = readXmlElementText(xpp, "datetimecompletion");
                experiment.setDateTimeDoneFromStringUTC(datetime);
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
            } else {
                skip(xpp);
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
    
    private DeviceInformation readDevice(XmlPullParser xpp) throws XmlPullParserException, IOException {
        Log.d("SensorDataCollector.UnitTest", "entered readDevice()");                

        xpp.require(XmlPullParser.START_TAG, XMLNS, "device");
        
        DeviceInformation deviceInfo = null; 
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("android")) {
                deviceInfo = readAndroidDevice(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return deviceInfo;        
    }
    
    private DeviceInformation readAndroidDevice(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "android");
        
        String make = null;
        String model = null;
        int sdkInt = -1;
        String sdkCodeName = null;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("make")) {
                make = readAndroidDeviceMake(xpp);
            } else if (name.equals("model")) {
                model = readAndroidDeviceModel(xpp);
            } else if (name.equals("sdk-int")) {
                sdkInt = readAndroidDeviceSdkInt(xpp);
            } else if (name.equals("sdk-codename")) {
                sdkCodeName = readAndroidDeviceSdkCodeName(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return new DeviceInformation(make, model, sdkInt, sdkCodeName);          
    }
    
    private String readAndroidDeviceMake(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "make");
    }
    
    private String readAndroidDeviceModel(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "model");
    }
    
    private int readAndroidDeviceSdkInt(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return Integer.parseInt(readXmlElementText(xpp, "sdk-int"));
    }
    
    private String readAndroidDeviceSdkCodeName(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "sdk-codename");
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
            } else {
                skip(xpp);
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
            } else {
                skip(xpp);
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
            } else {
                skip(xpp);
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
                Log.d("SensorDataCollector.UnitTest", "SimpleXMLFileStore::readSensors(): read a sensor");
            } else {
                skip(xpp);
            }
        }  

        return listSensors;
    }
    
    private AbstractSensor readSensor(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "sensor");
        
        Log.d("SensorDataCollector.UnitTest", "sensor id = " + xpp.getAttributeValue(XMLNS, "id"));
        Log.d("SensorDataCollector.UnitTest", "sensor count = " + xpp.getAttributeCount());
        Log.d("SensorDataCollector.UnitTest", "sensor name = " + xpp.getAttributeName(0));
        Log.d("SensorDataCollector.UnitTest", "sensor namespace = " + xpp.getAttributeNamespace(0));

        
        int id = Integer.parseInt(xpp.getAttributeValue("", "id"));
        
        String sensorName = null;
        int majorType = -1;
        int minorType = -1;
        AbstractStore.Channel channel = null;

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
            } else if (name.equals("datadescription")) {
                channel = readSensorDataDefinition(xpp);
            } else {
                skip(xpp);
            }
        } 
        
        AbstractSensor sensor = SensorUtilsSingleton.getInstance().getSensor(sensorName, majorType, minorType, id, channel);
        return sensor;
    }
    
    private AbstractStore.Channel readSensorDataDefinition(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "datadescription");
        
        String location = "";
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("location")) {
                location = readChannelLocation(xpp);
            } else {
                skip(xpp);
            }
        } 
        
        String channelFilePath = location; 
        
        return new SimpleFileStore.SimpleFileChannel(channelFilePath, AbstractStore.Channel.READ_ONLY);   
    }
    
    private String readChannelLocation(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "location");
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
}
