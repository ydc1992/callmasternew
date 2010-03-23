package cn.opda.service;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.opda.phone.WebBlack;

public class WebBlackHandler extends DefaultHandler {
	private List<WebBlack> blacks;
	private String preTag;
	private int version = 0;
	public int getVersion() {
		return version;
	}

	private WebBlack webBlack;

	public List<WebBlack> getBlacks() {
		return blacks;
	}

	@Override
	public void startDocument() throws SAXException {
		blacks = new ArrayList<WebBlack>();
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}


	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if("t".equals(localName)){
			webBlack = new WebBlack();
			webBlack.setNumber(attributes.getValue(0));
			webBlack.setType(attributes.getValue(1));
			webBlack.setRemark(attributes.getValue(2));
		}else if("main".equals(localName)){
			version = new Integer(attributes.getValue(0));
		}
		preTag = localName;
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if("t".equals(localName)){
			blacks.add(webBlack);
			webBlack = null;
		}else if("main".equals(localName)){
			version = 0;
		}
		preTag = null;
	}


	
	
	
}
