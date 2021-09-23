package com.example.market;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//rest방식으로 파싱하는 클래스

public class RestParser extends AsyncTask<List<ProductInfo>, Void, ArrayList<ProductInfo>> {
    private String url;
    public RestParser(String url) {
        this.url = url;
    }

    @Override
    protected ArrayList<ProductInfo> doInBackground(List<ProductInfo>... params) {

        ArrayList<ProductInfo> list = new ArrayList<>();
        Document doc = null;
        try {
            URL url1 = new URL(url);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(url1.openStream()));
            doc.getDocumentElement().normalize();
            NodeList itemNodeList = doc.getElementsByTagName("Product");

            for(int i=0; i<itemNodeList.getLength();i++){  //이건 11번가 xml파일 형식에 맞춘 파싱임, 다른 쇼핑몰은 새로 만들어야함
                Node node = itemNodeList.item(i);
                Element element = (Element) node;
                NodeList titleNodeList = element.getElementsByTagName("ProductCode");
                String code = titleNodeList.item(0).getChildNodes().item(0).getNodeValue();
                titleNodeList = element.getElementsByTagName("ProductName");
                String name = titleNodeList.item(0).getChildNodes().item(0).getNodeValue();
                titleNodeList = element.getElementsByTagName("ProductPrice");
                String price = titleNodeList.item(0).getChildNodes().item(0).getNodeValue();
                titleNodeList = element.getElementsByTagName("ProductImage200");
                String image = titleNodeList.item(0).getChildNodes().item(0).getNodeValue();

                list.add(new ProductInfo(code, name, price, image));
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<ProductInfo> L) {
        super.onPostExecute(L);
    }
}
