package cn.com.iresearch.ray.urlanalyz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.com.iresearch.ray.urlanalyz.URLAnalyzCli.URLObj;



public class Trie implements Serializable {
	public static class TrieNode implements Serializable {
		Map<Character, TrieNode> childNode;
		Object value;

		public void addChildNode(char c, TrieNode node) {
			if (node != childNode)
				childNode.put(c, node);
		}
		
		/*
		 * build new childnode if not exist
		 */
		public TrieNode buildNewNode(char c) {
			if (!childNode.containsKey(Character.valueOf(c))) {
				TrieNode tn = new TrieNode();
				childNode.put(Character.valueOf(c), tn);
				return tn;
			} else {
				return childNode.get(Character.valueOf(c));
			}
		}
		
		public TrieNode nextNBuild(char c)
        {
            if(!childNode.containsKey(Character.valueOf(c)))
            {
                TrieNode tn = new TrieNode();
                childNode.put(Character.valueOf(c), tn);
                return tn;
            } else
            {
                return childNode.get(Character.valueOf(c));
            }
        }

		public TrieNode next(char c) {
			return  childNode.get(Character.valueOf(c));
		}

		public TrieNode() {
			this.childNode = new HashMap<Character, TrieNode>();
			this.value = null;
		}

		public Map<Character, TrieNode> getChildNode() {
			return childNode;
		}

		public void setChildNode(Map<Character, TrieNode> childNode) {
			this.childNode = childNode;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

	public TrieNode root;

	public TrieNode getRoot() {
		return root;
	}

	public Trie() {
		root = null;
		root = new TrieNode();
	}
	
	public Trie(Map<String, URLObj> map) {
	    this.root = new TrieNode();

	    for (Map.Entry entry : map.entrySet())
	      insert((String)entry.getKey(), entry.getValue());
	  }
	
	/**
	 * Will a url into a branch of the trie, in the final insert url value of the last nodes
	 */
	private void insert(String url, Object value) {
		char[] charArray = url.toCharArray();
		if (charArray.length < 1)
			return;
		TrieNode node = this.root;
		for (int i = 0; i < charArray.length; i++) {
			node = node.buildNewNode(charArray[i]);
		}
		node.setValue(value);
	}
	
	public Object findLongestline(String key){
		char[] charArray = key.toCharArray();
		TrieNode nowNode = root;
		TrieNode cache =null;
		for (char c : charArray) {
			if(nowNode != null){
				if(nowNode.getValue()!=null)
					cache=nowNode;
				nowNode=nowNode.next(c);
			}else{
				if(cache!=null) return cache.getValue();
				else return null;
			}
		}
		 if(nowNode != null && nowNode.getValue() != null)
	            return nowNode.getValue();
	        if(cache != null)
	            return cache.getValue();
	        else
	            return null;
	}
	public Object find(String key)
    {
        char chars[] = key.toCharArray();
        TrieNode now = root;
        TrieNode cache =null;
        char arr$[] = chars;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            char c = arr$[i$];
            if(now != null)
                now = now.next(c);
            else
                return null;
        }

        if(now != null)
            return now.getValue();
        else
            return null;
    }
	
	// 能匹配到的最大字符串
	public synchronized String findLongestMatchline(String key){
		char[] charArray = key.toCharArray();
		TrieNode nowNode = root;
		TrieNode cache =null;
		
		//String.valueOf(charArray, 0, count);
		for (int i = 0; i < charArray.length; i++) {
			if (nowNode != null) {
				if (nowNode.getValue() != null){
					cache = nowNode;
				nowNode = nowNode.next(charArray[i]);}
			} else
				return String.valueOf(charArray, 0, i+1);
		}
		return null;
	}
}
