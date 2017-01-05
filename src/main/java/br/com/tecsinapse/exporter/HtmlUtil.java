package br.com.tecsinapse.exporter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

class HtmlUtil {
	static String htmlToText(String html) {
		EditorKit kit = new HTMLEditorKit();
		Document doc = kit.createDefaultDocument();
		doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
		try {
			kit.read(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), doc, 0);
			return doc.getText(0, doc.getLength());
		} catch (IOException | BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
}
