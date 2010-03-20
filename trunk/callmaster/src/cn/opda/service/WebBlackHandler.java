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
		/*if(webBlack!=null){
			if("name".equals(preTag)){
				webBlack.setName(new String(ch, start, length));
			}else if("age".equals(preTag)){
				webBlack.setAge(new Short(new String(ch, start, length)));
			}
		}*/
	}


	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		/*if("person".equals(localName)){
			webBlack = new WebBlack();
			webBlack.setNumber(number)
			setId(new Integer(attributes.getValue(0)));
		}
		preTag = localName;*/
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if("person".equals(localName)){
			blacks.add(webBlack);
			webBlack = null;
		}
		preTag = null;
	}


	
	
	
}
