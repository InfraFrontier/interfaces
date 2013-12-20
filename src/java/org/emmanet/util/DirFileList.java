package org.emmanet.util;

import java.io.File;
import java.io.FilenameFilter;

@Deprecated
/**
 * @Deprecated stuff
 */
public class DirFileList
{

    /**
     * @deprecated AjaxReturnController.FilenameFilterImpl replaces the functionality of the DirFileList class
     *             by returning only the qualifying files matching the filename pattern.
     */
    public DirFileList()
    {
        super();
        filter = new FilenameFilter() {

            public boolean accept(File dir, String name)
            {
                return !name.startsWith(".");
            }

            
            {
                //super();
            }
        };
    }

    public String[] filteredFileList(String dirName, String fileType)
    {
        dir = new File(dirName);
        children = dir.list(filter);
        
        if(children != null)
        {
            for(int i = 0; i < children.length; i++)
            {
                
                String filename = children[i];
                if(!filename.contains(fileType)){
                    children[i]=null;
                }
            }

        }
        return children;
    }

    private File dir;
    private String children[];
    FilenameFilter filter;
}

