package com.github.danilodant.hangman.repositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Repository
@Scope("singleton")
public class WordsRepository {

  private List<String> words;

  WordsRepository() {
    this.words = new ArrayList<>();
    loadWordsFromXml();
  }

  private void loadWordsFromXml() {
    try {
      Document doc = getXmlContent();
      
      NodeList childNodes = doc.getChildNodes();

      for (int index = 0; index < childNodes.getLength(); index++) {
        Node item = childNodes.item(index);
        NodeList childNodes2 = item.getChildNodes();

        for (int i = 0; i < childNodes2.getLength(); i++) {
          Node item2 = childNodes2.item(i);
          NodeList childNodes3 = item2.getChildNodes();

          for (int j = 0; j < childNodes3.getLength(); j++) {
            Node item3 = childNodes3.item(j);
            String word = item3.getTextContent().trim();
            if (word.length() > 0) {
              words.add(word);
            }
          }
        }
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  private Document getXmlContent() throws IOException, ParserConfigurationException, SAXException {
    final String FILENAME = "hangman.xml";
    ClassPathResource resource = new ClassPathResource(FILENAME);
    InputStream inputStream = resource.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String contents = reader.lines()
      .collect(Collectors.joining(System.lineSeparator()));

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

    DocumentBuilder db = dbf.newDocumentBuilder();

    InputSource inStream = new InputSource();
    inStream.setCharacterStream(new StringReader(contents));

    Document doc = db.parse(inStream);
    doc.getDocumentElement().normalize();
    return doc;
  }

  public List<String> getWords() {
    return words;
  }

  public String getRandomWord(){
    return this.words.get(new Random().nextInt(this.words.size()));
  }

}
