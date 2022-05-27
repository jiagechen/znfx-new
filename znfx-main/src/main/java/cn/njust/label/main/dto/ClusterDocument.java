package cn.njust.label.main.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program:
 * @description:
 * @
 **/
public abstract class ClusterDocument extends HashMap<Integer, ArrayList<Line>> {
    private int key;
    private ArrayList<Line> lineArrayList;
}
