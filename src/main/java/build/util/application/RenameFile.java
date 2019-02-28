package build.util.application;

import java.io.File;
import java.util.Arrays;

public class RenameFile {
	
	public static void main(String[] strings){/*
		File dir = new File ("C:\\Personal\\Technical\\Javascript\\JavaScript Objects and Prototypes In-depth\\temp");
		String des = "C:\\Personal\\Technical\\Javascript\\JavaScript Objects and Prototypes In-depth\\temp2\\";
		for(File tmp : dir.listFiles()){
			String name = tmp.getName();
			System.out.println(name);
			System.out.println(des + name.substring(0,(name.indexOf(" - YouTube (360p)"))));
			
			File newFile = new File (des + name.substring(0,(name.indexOf(" - YouTube (360p)")))+".mp4");
			if(tmp.renameTo(newFile)){
				System.out.println("success");
			}else{
				System.out.println("failure");
			}
		}
	*/		String s1 = new String("Hello");
	
	String s2 = s1.intern();
	//String s3 = "Hello";
	
	System.out.println(s1 == s2);
//	System.out.println(s3 == s2);

		

	}

}
