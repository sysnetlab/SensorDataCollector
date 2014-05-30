package sysnetlab.android.sdc.datastore;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;
import sysnetlab.android.sdc.datacollector.Experiment;
import sysnetlab.android.sdc.datacollector.Note;
import sysnetlab.android.sdc.datacollector.Tag;
import sysnetlab.android.sdc.datacollector.TaggingAction;
import sysnetlab.android.sdc.sensor.AbstractSensor;

public class SimpleXMLFileStore extends SimpleFileStore {

    private final String XMLNS = "http://schemas.sysnetlab.net/apps/sdc";
    private final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    private final String XSI_SCHEMA_LOCATION = "http://schemas.sysnetlab.net/apps/sdc ExperimentMetaData.xsd";

    public SimpleXMLFileStore() throws RuntimeException {
    }

    @Override
    public List<Experiment> listStoredExperiments() {
        // TODO Auto-generated method stub
        return super.listStoredExperiments();
    }

    @Override
    public void writeExperimentMetaData(Experiment experiment) {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        Log.e("SensorDataCollector.UnitTest", "xmlSerializer = <" + xmlSerializer + ">");
        
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

            xmlSerializer.startTag("", "make");
            xmlSerializer.text(experiment.getDeviceInformation().getModel());
            xmlSerializer.endTag("", "make");

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

            String configFilePath = getNewExperimentPath() + "/.experiment.xml";
            PrintStream out;
            out = new PrintStream(new BufferedOutputStream(new FileOutputStream(configFilePath)));
            
            out.println(stringWriter.toString());
            out.close();
            Log.e("SensorDataCollector.UnitTest", "xml file = <" + stringWriter.toString() + ">");
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
