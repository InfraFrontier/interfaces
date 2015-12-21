package org.emmanet.util;

/*
 * #%L
 * InfraFrontier
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 EMBL-European Bioinformatics Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

