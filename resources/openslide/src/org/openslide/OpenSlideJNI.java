package org.openslide;


import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

class OpenSlideJNI {
	
	static public void main(String argv[]) {
	    OpenSlideJNI op = new OpenSlideJNI();
        System.out.println(op.openslide_get_version()); 
        String file = "/home/ravikiran/SI-09.bif";
        File f = new File(file);
        if(!f.exists())
        	return;
        String ret = op.openslide_detect_vendor(file);
        System.out.println(ret);
        long osr = op.openslide_open(file);
        String[] props = op.openslide_get_property_names(osr);
        
        System.out.println("count "+OpenSlideJNI.openslide_level_count(osr));
    }
	
    private OpenSlideJNI() {
    }

    static {
    	String libraryPath = null;
    	try {
    		libraryPath = NativeUtils.loadLibraryFromJar("/lib/libctest.so");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //libraryPath = "/home/ravikiran/scanner/openslide/testjava/src/libctest.so";
        System.load(libraryPath);
        /*try {
            InputStream is = OpenSlideJNI.class.getClassLoader().
                    getResourceAsStream("resources/openslide.properties");
            if (is != null) {
            	System.out.println("not null");
                Properties p = new Properties();
                p.load(is);
                libraryPath = p.getProperty("openslide.jni.path");
                if (libraryPath.equals("")) {
                    libraryPath = null;
                }
            }
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        if (libraryPath != null) {
            System.load(libraryPath);
        } else {
            System.loadLibrary("openslide-jni");
        }*/
    }

    native static String openslide_get_version();
    native static String openslide_detect_vendor(String file);
    native static long openslide_open(String file);
    native static void openslide_get_level_dimensions(long osr, int level, long dim[]);
    native static double openslide_get_level_downsample(long osr, int level);
    native static void openslide_close(long osr);
    native static String[] openslide_get_property_names(long osr);
    native static String openslide_get_property_value(long osr, String name);
    native static String[] openslide_get_associated_image_names(long osr);
    native static void openslide_read_region(long osr, int dest[], long x,long y, int level, long w, long h);
    native static void openslide_get_associated_image_dimensions(long osr,String name, long dim[]);
    native static void openslide_read_associated_image(long osr, String name,int dest[]);
    native static String openslide_get_error(long osr);
    native static int openslide_level_count(long osr);
}