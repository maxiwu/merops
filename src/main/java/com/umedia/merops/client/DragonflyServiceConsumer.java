package com.umedia.merops.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestOperations;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.umedia.merops.DragonflyException;
import com.umedia.merops.IDragonflyService;

public class DragonflyServiceConsumer implements IDragonflyService {

	private String dragonflyDeviceListURL;
	@Autowired
	private RestOperations dragonflyRestTemplate;
	
	@Override
	public List<String> getDevices() throws DragonflyException {
		// TODO Auto-generated method stub
		//String list = getDragonflyRestTemplate().getForObject(URI.create(dragonflyDeviceListURL), String.class);
		//String list = getDragonflyRestTemplate().getForObject(URI.create(dragonflyDeviceListURL), String.class);
		try{
		InputStream photosXML = new ByteArrayInputStream(getDragonflyRestTemplate().getForObject(
				URI.create(dragonflyDeviceListURL), byte[].class));

		
		final List<String> photoIds = new ArrayList<String>();
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setValidating(false);
		parserFactory.setXIncludeAware(false);
		parserFactory.setNamespaceAware(false);
		SAXParser parser = parserFactory.newSAXParser();
		parser.parse(photosXML, new DefaultHandler() {
			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {
				if ("photo".equals(qName)) {
					photoIds.add(attributes.getValue("id"));
				}
			}
		});
		//return photoIds;
		}catch (Exception e)
		{
			return null;
		}
		//should return json or xml, then parse it to a list<string>
		return null;
	}

	@Override
	public String readDeviceStatus(String deviceId) throws DragonflyException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDragonflyDeviceListURL() {
		return dragonflyDeviceListURL;
	}

	public void setDragonflyDeviceListURL(String dragonflyDeviceListURL) {
		this.dragonflyDeviceListURL = dragonflyDeviceListURL;
	}

	public RestOperations getDragonflyRestTemplate() {
		return dragonflyRestTemplate;
	}

	public void setDragonflyRestTemplate(RestOperations dragonflyRestTemplate) {
		this.dragonflyRestTemplate = dragonflyRestTemplate;
	}

}
