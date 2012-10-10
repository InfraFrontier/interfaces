/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.util.DirFileList;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class FileListController  extends SimpleFormController{
    
     @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
         String submissionFileType=request.getParameter("type");
        String action = request.getParameter("action");
        DirFileList fl=new DirFileList();
        
        if(action.equals("list")){
            System.out.println("File list requested");
            String[] list = fl.filteredFileList("",submissionFileType);
            
            //// Growable list
List li = new LinkedList(Arrays.asList(list));
        }
         
          return null;
        //  return new ModelAndView(getSuccessView(), );
     }
   
}
