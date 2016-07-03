/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.validate;

import java.util.ArrayList;

import org.gedcom4j.io.encoding.Encoding;
import org.gedcom4j.model.*;
import org.junit.Test;

/**
 * Test case for {@link HeaderValidator}
 * 
 * @author mharrah
 * 
 */
public class HeaderValidatorTest extends AbstractValidatorTestCase {

    /**
     * Test for character set in header
     */
    @Test
    public void testCharacterSet() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.getSubmitters().put(s.xref, s);
        g.setSubmission(new Submission("@SUBN0001@"));
        Header h = g.getHeader();
        h.submitter = s;
        h.submission = g.getSubmission();

        h.characterSet = null;

        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set");

        h.characterSet = new CharacterSet();
        rootValidator.validate();
        assertNoIssues();

        h.characterSet.characterSetName = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set", "name", "not", "defined");

        h.characterSet.characterSetName = new StringWithCustomTags("FRYINGPAN");
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "character set", "not", "supported");

        h.characterSet.characterSetName = new StringWithCustomTags(Encoding.ASCII.getCharacterSetName());
        rootValidator.validate();
        assertNoIssues();

    }

    /**
     * Test for copyright data in header
     */
    @Test
    public void testCopyrightData() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.getSubmitters().put(s.xref, s);
        Header h = g.getHeader();
        h.submitter = s;
        g.setSubmission(new Submission("@SUBN0001@"));

        h.copyrightData = null;
        h.submission = g.getSubmission();

        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "copyright");

        h.copyrightData = new ArrayList<String>();

        rootValidator.validate();
        assertNoIssues();

    }

    /**
     * Test for Gedcom version in header
     */
    @Test
    public void testGedcomVersion() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.getSubmitters().put(s.xref, s);
        Header h = g.getHeader();
        h.submitter = s;
        g.setSubmission(new Submission("@SUBN0001@"));
        h.submission = g.getSubmission();

        h.gedcomVersion = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "gedcom", "version");

        h.gedcomVersion = new GedcomVersion();
        rootValidator.validate();
        assertNoIssues();

        h.gedcomVersion.versionNumber = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "gedcom", "version", "number");
    }

    /**
     * Test header without submitters
     */
    @Test
    public void testNoSubmitters() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        g.setSubmission(new Submission("@SUBN0001@"));
        g.getHeader().submission = g.getSubmission();

        rootValidator.autorepair = false;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "submitter", "not specified");

        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.getSubmitters().put(s.xref, s);
        Header h = g.getHeader();
        h.submitter = s;

        rootValidator.validate();
        assertNoIssues();

    }

    /**
     * Test for sourceSystem in header
     */
    @Test
    public void testSourceSystem() {
        Gedcom g = new Gedcom();
        rootValidator.gedcom = g;
        rootValidator.autorepair = false;
        Submitter s = new Submitter();
        s.xref = "@SUBM0001@";
        s.name = new StringWithCustomTags("test");
        g.getSubmitters().put(s.xref, s);
        Header h = g.getHeader();
        h.submitter = s;
        g.setSubmission(new Submission("@SUBN0001@"));
        h.submission = g.getSubmission();

        h.sourceSystem = null;

        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "source system", "specified");

        h.sourceSystem = new SourceSystem();
        rootValidator.validate();
        assertNoIssues();

        h.sourceSystem.corporation = null;
        rootValidator.validate();
        assertNoIssues();

        h.sourceSystem.corporation = new Corporation();
        rootValidator.validate();
        assertNoIssues();

        h.sourceSystem.corporation.businessName = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "source system", "corporation", "name");

        h.sourceSystem.corporation.businessName = "Frying Pan";
        rootValidator.validate();
        assertNoIssues();

        h.sourceSystem.productName = new StringWithCustomTags("Yo");
        rootValidator.validate();
        assertNoIssues();

        h.sourceSystem.productName = null;
        rootValidator.validate();
        assertNoIssues();

        h.sourceSystem.systemId = null;
        rootValidator.validate();
        assertFindingsContain(Severity.ERROR, "source system", "system", "id");

        h.sourceSystem.systemId = "Test";
        rootValidator.validate();
        assertNoIssues();
    }
}
