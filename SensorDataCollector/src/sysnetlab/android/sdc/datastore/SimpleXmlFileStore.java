/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

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
import android.widget.ProgressBar;
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
import sysnetlab.android.sdc.sensor.SensorDiscoverer;
import sysnetlab.android.sdc.sensor.audio.AudioChannelIn;
import sysnetlab.android.sdc.sensor.audio.AudioEncoding;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioSensor;
import sysnetlab.android.sdc.sensor.audio.AudioSensorHelper;
import sysnetlab.android.sdc.sensor.audio.AudioSource;

public class SimpleXmlFileStore extends SimpleFileStore {

    private final String XMLNS = "http://schemas.sysnetlab.net/apps/sdc";
    private final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    private final String XSI_SCHEMA_LOCATION = "http://schemas.sysnetlab.net/apps/sdc ExperimentMetaData.xsd";
    private final String XML_EXPERIMENT_META_DATA_FILE = "experiment.xml";

    public SimpleXmlFileStore() throws RuntimeException {
        super();
    }
    
    @Override
    public List<Experiment> listStoredExperiments(ProgressBar mProgressBar) {
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
            mProgressBar.setProgress(i);
        }
        return listExperiments;
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
            // Log.d("SensorDataCollector", "xml file = [ " + stringWriter.toString() + " ]");
        } catch (IllegalArgumentException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector", "SimpleXmlFileStore::writeExperimentMetaData(): " + e.toString());
        } catch (IllegalStateException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector", "SimpleXmlFileStore::writeExperimentMetaData(): " + e.toString());
        } catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector", "SimpleXmlFileStore::writeExperimentMetaData(): " + e.toString());
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
            Log.e("SensorDataCollector", "SimpleXmlFileStore::writeExperimentMetaData(): " + e.toString());
        } 
    }
    
    public Experiment loadExperiment(String experimentPath) {
        // Log.d("SensorDataCollector.UnitTest", "entered loadExperiment().");        
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
            Log.e("SensorDataCollector",
                    "SimpleXmlFileStore()::loadExperiment(): configFilePath = " + configFilePath
                            + " : " + e.toString());
        } catch (XmlPullParserException e) {
            experiment = null;
            Log.e("SensorDataCollector",
                    "SimpleXmlFileStore()::loadExperiment(): configFilePath = " + configFilePath
                            + " : " + e.toString());            
        } catch (IOException e) {
            experiment = null;
            Log.e("SensorDataCollector",
                    "SimpleXmlFileStore()::loadExperiment(): configFilePath = " + configFilePath
                            + " : " + e.toString());         
        } catch (IllegalStateException e) {
            experiment = null;
            Log.e("SensorDataCollector",
                    "SimpleXmlFileStore()::loadExperiment(): configFilePath = " + configFilePath
                            + " : " + e.toString());
        } catch (ParseException e) {
            experiment = null;
            Log.e("SensorDataCollector",
                    "SimpleXmlFileStore()::loadExperiment(): configFilePath = " + configFilePath
                            + " : " + e.toString());
        } 
        
        Log.d("SensorDataCollector", "SimpleXmlFileStore::loadExperiment(): loaded experiment ["
                + (experiment != null ? experiment.toString() : "null") + "]");
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

        xs.startTag("", "sdk-release");
        xs.text(deviceInfo.getSdkRelease());
        xs.endTag("",  "sdk-release");
        
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
                
                xs.startTag("", "number");
                xs.text(Integer.toString(tag.getTagId()));
                xs.endTag("", "number");
                
                
                if (tag.getShortDescription() != null && tag.getShortDescription().trim().length() > 0) {
                    xs.startTag("", "shortdescription");
                    xs.text(tag.getShortDescription());
                    xs.endTag("", "shortdescription");
                }

                if (tag.getLongDescription() != null && tag.getLongDescription().trim().length() > 0) {
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
                xs.attribute("", "number", Integer.toString(action.getTag().getTagId()));
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
                    case AbstractSensor.AUDIO_SENSOR:
                        serializeAudioSensor(xs, (AudioSensor) sensor);
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
        xs.attribute("",  "type",  getChannelTypeString(sensor.getChannel().getType()));

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
    
    private void serializeAudioSensor(XmlSerializer xs, AudioSensor sensor) throws IllegalArgumentException, IllegalStateException, IOException {
        xs.startTag("",  "datadescription");
        xs.attribute("",  "type",  getChannelTypeString(sensor.getChannel().getType()));

        xs.startTag("",  "location");
        xs.text(sensor.getChannel().describe());
        xs.endTag("",  "location");
        
        xs.startTag("", "source");
        xs.startTag("", "id");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getSource().getSourceId()));
        xs.endTag("", "id");
        // Resource Id changes when the string.xml is updated. It is not good to use them in 
        // persistent manner. 
        /*
        xs.startTag("", "nameresid");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getSource().getSourceNameResId()));
        xs.endTag("", "nameresid");
        */
        xs.endTag("",  "source");
        
        xs.startTag("", "channelin");
        xs.startTag("", "id");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getChannel().getChannelId()));
        xs.endTag("", "id");
        /*
        xs.startTag("", "nameresid");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getChannel().getChannelNameResId()));
        xs.endTag("", "nameresid");
        */
        xs.endTag("",  "channelin");
        
        xs.startTag("", "encoding");
        xs.startTag("", "id");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getEncoding().getEncodingId()));
        xs.endTag("", "id");
        /*
        xs.startTag("", "nameresid");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getEncoding().getEncodingNameResId()));
        xs.endTag("", "nameresid");
        */
        xs.endTag("",  "encoding");
        
        xs.startTag("", "samplingrate");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getSamplingRate()));
        xs.endTag("",  "samplingrate");    
        
        xs.startTag("", "buffersize");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getBufferSize()));
        xs.endTag("",  "buffersize");  
        
        xs.startTag("", "minbuffersize");
        xs.text(Integer.toString(sensor.getAudioRecordParameter().getMinBufferSize()));
        xs.endTag("",  "minbuffersize");  
        
        xs.endTag("",  "datadescription");
    }
    
    private String getChannelTypeString(int type) {
        String strType;
        
        if (type == AbstractStore.Channel.CHANNEL_TYPE_CSV) {
            strType = "csv";
        } else if (type == AbstractStore.Channel.CHANNEL_TYPE_BIN) {
            strType = "bin";
        } else if (type == AbstractStore.Channel.CHANNEL_TYPE_PCM) {
            strType = "pcm";
        } else if (type == AbstractStore.Channel.CHANNEL_TYPE_WAV) {
            strType = "wav";
        } else {
            strType = "txt";
        }
        
        return strType;
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

            // Log.d("SensorDataCollector", "SimpleXmlFileStore::readExperiment(): element = " + elem);

            if (elem.equals("name")) {
                String name = readXmlElementText(xpp, "name");
                experiment.setName(name);
                // Log.d("SensorDataCollector", "readExperiment(): " + xpp.getText());                
            } else if (elem.equals("device")) {
                DeviceInformation device = readDevice(xpp);
                experiment.setDeviceInformation(device);
                // Log.d("SensorDataCollector", "readExperiment(): " + device.toString());                
            } else if (elem.equals("datetimecreation")) {
                String datetime = readXmlElementText(xpp, "datetimecreation");
                experiment.setDateTimeCreatedFromStringUTC(datetime);
                // Log.d("SensorDataCollector", "readExperiment(): " + xpp.getText());
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
        
        /*
        String dbgString = " name = " + experiment.getName() +  
                " datetimecreated = " + experiment.getDateTimeCreatedAsStringUTC() +
                " datetimecompleted = " + experiment.getDateTimeDoneAsStringUTC();
        for (Tag tag : experiment.getTags()) {
            dbgString += " Tag = (" + tag.getName() + ", " + tag.getShortDescription() + ", " + tag.getLongDescription() + ") ";
        }

        Log.d("SensorDataCollector", "SimpleXmlFileStore():readExperiment(): " + dbgString);
        */
        
        return experiment;
    }
    
    private DeviceInformation readDevice(XmlPullParser xpp) throws XmlPullParserException, IOException {
        // Log.d("SensorDataCollector", "entered readDevice()");                

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
        String sdkRelease = null;
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
            } else if (name.equals("sdk-release")) {
                sdkRelease = readAndroidDeviceSdkRelease(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return new DeviceInformation(make, model, sdkInt, sdkCodeName, sdkRelease); 
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
    
    private String readAndroidDeviceSdkRelease(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "sdk-release");
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
        int tagId = 0;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();
            if (name.equals("name")) {
                tagName = readTagName(xpp);
            }else if (name.equals("tagId")){
            	tagId = readTagId(xpp);
            } else if (name.equals("shortdescription")) {
                shortDescription = readShortDescription(xpp);
            } else if (name.equals("longdescription")) {
                longDescription = readLongDescription(xpp);
            } else {
                skip(xpp);
            }
        }
        
        return new Tag(tagName, shortDescription, longDescription, tagId);
    }
    
    private String readTagName(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return readXmlElementText(xpp, "name");
    }
    
    private int readTagId(XmlPullParser xpp) throws XmlPullParserException, IOException{
    	return Integer.parseInt(readXmlElementText(xpp, "tagId"));
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
                int tagId = Integer.parseInt(xpp.getAttributeValue(XMLNS, "id"));
                taggingTag = null;
                for (Tag tag : listTags) {
                    if (tag.getName().equals(tagReference)) {
                        taggingTag = tag;
                        break;
                    }
                }
                if (taggingTag == null) {
                    taggingTag = new Tag(tagReference, tagId);
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
                // Log.d("SensorDataCollector", "SimpleXMLFileStore::readSensors(): read a sensor");
            } else {
                skip(xpp);
            }
        }  

        return listSensors;
    }
    
    private class AudioDataDefinition {
        AudioRecordParameter mAudioDataParameter;
        AbstractStore.Channel mChannel;
    }
    
    private AbstractSensor readSensor(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "sensor");
        
        /*
        Log.d("SensorDataCollector", "SimpleXmlFileStore::readSensor(): sensor id = " + xpp.getAttributeValue(XMLNS, "id"));
        Log.d("SensorDataCollector", "SimpleXmlFileStore::readSensor(): sensor count = " + xpp.getAttributeCount());
        Log.d("SensorDataCollector", "SimpleXmlFileStore::readSensor(): sensor name = " + xpp.getAttributeName(0));
        Log.d("SensorDataCollector", "SimpleXmlFileStore::readSensor(): sensor namespace = " + xpp.getAttributeNamespace(0));
        */
        
        int id = Integer.parseInt(xpp.getAttributeValue("", "id"));
        
        String sensorName = null;
        int majorType = -1;
        int minorType = -1;
        AbstractStore.Channel channel = null;
        int channelType = -1;
        
        AudioDataDefinition audioDataDefinition = new AudioDataDefinition();
        
        AbstractSensor sensor = null;

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
                channelType = readChannelType(xpp);
                switch (majorType) {
                    case AbstractSensor.ANDROID_SENSOR:
                        channel = readAndroidSensorDataDefinition(xpp, channelType);
                        break;
                    case AbstractSensor.AUDIO_SENSOR:
                        audioDataDefinition = readAudiodSensorDataDefinition(xpp, channelType);
                        break;
                    default:
                        Log.w("SensorDataCollector",
                                "SimpleXmlFileStore::readSensor(): unsupported sensor major type "
                                        + majorType);
                        break;
                }
            } else {
                skip(xpp);
            }
        } 
        
        switch (majorType) {
            case AbstractSensor.ANDROID_SENSOR:
                sensor = SensorDiscoverer.constructSensorObject(sensorName, majorType, minorType,
                        id, channel, null);
                break;
            case AbstractSensor.AUDIO_SENSOR:
                sensor = SensorDiscoverer.constructSensorObject(sensorName, majorType, minorType,
                        id, audioDataDefinition.mChannel, audioDataDefinition.mAudioDataParameter);
                break;
            default:
                sensor = null;
                Log.w("SensorDataCollector",
                        "SimpleXmlFileStore::readSensor(): unsupported sensor major type "
                                + majorType);
                break;
        }
        return sensor;
    }
    
    private int readChannelType(XmlPullParser xpp) throws XmlPullParserException, IOException {
        int type = -1;
        
        xpp.require(XmlPullParser.START_TAG, XMLNS, "datadescription");
        
        String strType = xpp.getAttributeValue("", "type"); 
        if ("csv".equals(strType)) {
            type = AbstractStore.Channel.CHANNEL_TYPE_CSV;
        } else if ("bin".equals(strType)) {
            type = AbstractStore.Channel.CHANNEL_TYPE_BIN;
        } else if ("pcm".equals(strType)) {
            type = AbstractStore.Channel.CHANNEL_TYPE_PCM;
        } else if ("wav".equals(strType)) {
            type = AbstractStore.Channel.CHANNEL_TYPE_WAV;
        }
        
        return type;
    }
    
    private AbstractStore.Channel readAndroidSensorDataDefinition(XmlPullParser xpp, int channelType) throws XmlPullParserException, IOException {
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
        
        return new SimpleFileStore.SimpleFileChannel(channelFilePath, AbstractStore.Channel.READ_ONLY, channelType);   
    }
    
    private AudioDataDefinition readAudiodSensorDataDefinition(XmlPullParser xpp, int channelType) throws XmlPullParserException, IOException {
        AudioDataDefinition audioDataDefinition = new AudioDataDefinition();
        
        xpp.require(XmlPullParser.START_TAG, XMLNS, "datadescription");
        
        String location = "";
        
        AudioSource s = null;
        AudioChannelIn c = null;
        AudioEncoding e = null;
        int r = -1;
        int bs = -1;
        int mbs = -1;
        
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("location")) {
                location = readChannelLocation(xpp);
            } else if (name.equals("source")) {
                s = readAudioSource(xpp);
            } else if (name.equals("channelin")) {
                c = readAudioChannelIn(xpp);
            } else if (name.equals("encoding")) {
                e = readAudioEncoding(xpp);
            } else if (name.equals("samplingrate")) {
                r = Integer.parseInt(readXmlElementText(xpp, "samplingrate"));
            } else if (name.equals("buffersize")) {
                bs = Integer.parseInt(readXmlElementText(xpp, "buffersize"));
            } else if (name.equals("minbuffersize")) {
                mbs = Integer.parseInt(readXmlElementText(xpp, "minbuffersize"));
            } else {
                skip(xpp);
            }
        } 
        
        audioDataDefinition.mChannel = new SimpleFileStore.SimpleFileChannel(location, AbstractStore.Channel.READ_ONLY, channelType);
        audioDataDefinition.mAudioDataParameter = new AudioRecordParameter(r, c, e, s, bs, mbs);
        
        return audioDataDefinition;
    }    
    
    private AudioSource readAudioSource(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "source");
        
        int id = -1;
        int nameresid = -1;
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("id")) {
                id = Integer.parseInt(readXmlElementText(xpp, "id"));
                nameresid = AudioSensorHelper.getSourceNameResId(id);
            } /*else if (name.equals("nameresid")) {
                nameresid = Integer.parseInt(readXmlElementText(xpp, "nameresid"));
            } */ else {
                skip(xpp);
            }
        } 
        
        return new AudioSource(id, nameresid);       
    }
    
    private AudioChannelIn readAudioChannelIn(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "channelin");
        
        int id = -1;
        int nameresid = -1;
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("id")) {
                id = Integer.parseInt(readXmlElementText(xpp, "id"));
                nameresid = AudioSensorHelper.getChannelInNameResId(id);
            } /* else if (name.equals("nameresid")) {
                nameresid = Integer.parseInt(readXmlElementText(xpp, "nameresid"));
            } */ else {
                skip(xpp);
            }
        } 
        
        return new AudioChannelIn(id, nameresid);       
    }
    
    private AudioEncoding readAudioEncoding(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.require(XmlPullParser.START_TAG, XMLNS, "encoding");
        
        int id = -1;
        int nameresid = -1;
        while (xpp.next() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = xpp.getName();

            if (name.equals("id")) {
                id = Integer.parseInt(readXmlElementText(xpp, "id"));
                nameresid = AudioSensorHelper.getEncodingNameResId(id);
            } /* else if (name.equals("nameresid")) {
                nameresid = Integer.parseInt(readXmlElementText(xpp, "nameresid"));
            } */ else {
                skip(xpp);
            }
        } 
        
        return new AudioEncoding(id, nameresid);       
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
