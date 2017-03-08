package cn.com.ire.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class FileHandler
{

    private static final Logger logger = Logger.getLogger(FileHandler.class);


    public static List readFileToList(InputStream is)
        throws IOException
    {
        return readFileToList(is, false);
    }

    public static List readFileToList(InputStream is, boolean doesRemoveBomOfUTF8)
        throws IOException
    {
        List stringList = new ArrayList();
        BufferedReader input = new BufferedReader(new InputStreamReader(is, "utf-8"));
        do
        {
            String line = input.readLine();
            if(line != null)
            {
                if(doesRemoveBomOfUTF8)
                    line = removeBOMofUTF8(line);
                stringList.add(line);
            } else
            {
                input.close();
                return stringList;
            }
        } while(true);
    }

    public static List readFileToList(String fileName)
        throws IOException
    {
        return readFileToList(fileName, false);
    }

    public static List readFileToList(String fileName, boolean doesRemoveBomOfUTF8)
        throws IOException
    {
        return readFileToList(((InputStream) (new FileInputStream(fileName))), doesRemoveBomOfUTF8);
    }

    public static String readFileToString(InputStream is)
        throws IOException
    {
        return readFileToString(is, false);
    }

    public static String readFileToString(InputStream is, boolean doesRemoveBomOfUTF8)
        throws IOException
    {
        List stringList = readFileToList(is, doesRemoveBomOfUTF8);
        StringBuffer sb = new StringBuffer();
        String string;
        for(Iterator i$ = stringList.iterator(); i$.hasNext(); sb.append(string))
            string = (String)i$.next();

        return sb.toString();
    }

    public static String readFileToString(String fileName)
        throws IOException
    {
        return readFileToString(fileName, false);
    }

    public static String readFileToString(String fileName, boolean doesRemoveBomOfUTF8)
        throws IOException
    {
        return readFileToString(((InputStream) (new FileInputStream(fileName))), doesRemoveBomOfUTF8);
    }

    public static void writeListToFile(List stringList, String fileName)
        throws IOException
    {
        try
        {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            StringBuffer sb = new StringBuffer();
            String string;
            for(Iterator i$ = stringList.iterator(); i$.hasNext(); sb.append((new StringBuilder()).append(string).append("\n").toString()))
                string = (String)i$.next();

            output.write(sb.toString());
            output.close();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
            throw e;
        }
        catch(FileNotFoundException e)
        {
            logger.error((new StringBuilder()).append("File Not Found: ").append(fileName).toString(), e);
            e.printStackTrace();
            throw e;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static String removeBOMofUTF8(String text)
    {
        String BOM = String.valueOf('\uFEFF');
        return text.startsWith(BOM) ? text.substring(BOM.length(), text.length()) : text;
    }

}
