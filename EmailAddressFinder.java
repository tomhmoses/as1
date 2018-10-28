//package com.bham.pij.assignments.a1a;

// Tom Moses 1911437

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EmailAddressFinder {
    
    private static ArrayList<String> emailAddresses;
    
    public static void main(String[] args) {
        emailAddresses = new ArrayList<String>();
        EmailAddressFinder eaf = new EmailAddressFinder();
        eaf.run();
        System.out.println("Email addresses found: " + emailAddresses.size());
    }
    
    public void run() {
        
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader("corrupteddb.txt"));
     
            String input = "";
           
            PrintWriter pw = new PrintWriter("eaf");
            
            while ((input = reader.readLine()) != null) {
                
                input = input.trim();
                
                ArrayList<String> temp = new ArrayList<String>();
                
                temp = findEmailAddresses(input);             
                
                for (String t: temp) {
                    emailAddresses.add(t);
                }                
            }
            
            pw.close();
            reader.close();
        }
        
        catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    public ArrayList<String> findEmailAddresses(String input) {
        
        String text = input;

        ArrayList<String> list = new ArrayList<String>();
        
        /*
         * Your code goes here.
         */

        int startLookingAt = 0;

        while (getEndOfEmail(text,startLookingAt) != -1) {
            
            int endPosition = getEndOfEmail(text,startLookingAt);
            String email = text.substring(0,endPosition);
            email = findValidEmail(email);

            if (email.equals("")) {
                startLookingAt = endPosition;
            } else {
                list.add(email);
                System.out.println(email);
                text = text.substring(endPosition);
                startLookingAt = 0;
            }
        
        }
        
        return list;
    }
    
    public static int getEndOfEmail(String input, int startLookingAt) {

        String[] endings = {".com", ".jp", ".ro", ".uk", ".de", ".net"};
        
        //will find the first occurrence of email end
        int foundPosition = -1;
        String found = "";
        for (int i = 0; i < endings.length; i++) {
            int tempPos = input.indexOf(endings[i], startLookingAt);
            if (tempPos != -1 && (foundPosition > tempPos || foundPosition == -1)) {
                foundPosition = tempPos;
                found = endings[i];
            }
        }
        int endPosition = foundPosition + found.length();
        
        
        return endPosition;

    }

    public static String findValidEmail(String email) {

        int position = email.length() - 1;
        boolean foundAt = false;
        boolean foundLocalPeriod = false;
        boolean foundInvalidChar = false;
        


        while (position >= 0 && !foundInvalidChar) {

            
            char currentChar = email.charAt(position);

            if (!foundAt) {
                if (currentChar == '@') {
                    foundAt = true;
                }
                else if (!(currentChar == '.' || Character.isLowerCase(currentChar))) {
                    foundInvalidChar = true;
                }
            }
            else {
                if (currentChar == '.') {
                    if (!foundLocalPeriod) {
                        foundLocalPeriod = true;
                    }
                    else {
                        foundInvalidChar = true;
                    } 
                }
                else if (!(Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_')) {
                    foundInvalidChar = true;
                }
            }

            if (!foundInvalidChar) {
                position--;
            }
        }

        if (!foundAt) {
            email = "";
            return email;
        }
        else if (foundInvalidChar) {
            email = email.substring(position + 1);
        }


        while (email.charAt(0) == '.') {
            email = email.substring(1);
        }
        if (email.charAt(0) == '@') {
            email = "";
            return email;
        }

        return email;

    }
}
