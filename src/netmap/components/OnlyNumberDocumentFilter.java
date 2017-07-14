/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmap.components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

/**
 * Document Filter for only numbers allowed on input
 * @author Darlan
 */
public class OnlyNumberDocumentFilter extends DocumentFilter
{

    @Override
    public void insertString(FilterBypass fb, int offset, String text,
            AttributeSet attr) throws BadLocationException
    {
        text = text.replaceAll("\\D+","");
        super.insertString(fb, offset, text, attr);

    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text,
            AttributeSet attrs) throws BadLocationException
    {
        text = text.replaceAll("\\D+","");
        super.replace(fb, offset, length, text, attrs);

    }
}
