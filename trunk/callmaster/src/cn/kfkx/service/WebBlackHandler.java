package cn.kfkx.service;

import java.util.ArrayList;
import java.util.List;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.kfkx.phone.WebBlack;


public class WebBlackHandler extends DefaultHandler {
	private List<WebBlack> blacks;
	private String preTag;
	private int version = 0;
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
		if("version".equals(preTag)){
			version = Integer.parseInt(new String(ch, start, length));
		}
	}


	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		if("t".equals(localName)){
			webBlack = new WebBlack();
			webBlack.setNumber(attributes.getValue(0));
			String temp = "0x"+attributes.getValue(1);
			int type = Integer.decode(temp).intValue();
			
			if(TagUtils.CheackTags(type, TagUtils.yishengxiang)){
				webBlack.setType(WebBlack.TYPE_ONESOUND);
			}else if(TagUtils.CheackTags(type, TagUtils.gaoe)){
				webBlack.setType(WebBlack.TYPE_OVERCHARGE);
			}else if(TagUtils.CheackTags(type, TagUtils.tuixiao)){
				webBlack.setType(WebBlack.TYPE_PROMOTION);
			}else if(TagUtils.CheackTags(type, TagUtils.saorao)){
				webBlack.setType(WebBlack.TYPE_OTHER);
			}else if(TagUtils.CheackTags(type, TagUtils.message)){
				webBlack.setType(WebBlack.TYPE_MESSAGE);
			}else{
				webBlack.setType(WebBlack.TYPE_ONESOUND);
			}
			webBlack.setRemark(attributes.getValue(2));
		}
		preTag = localName;
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if("t".equals(localName)){
			blacks.add(webBlack);
			webBlack = null;
		}else if("version".equals(localName)){
			
		}
		preTag = null;
	}


	
	
	
}
