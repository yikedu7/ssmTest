package cn.damonto.ssmTest.common.tree;

import cn.damonto.ssmTest.entity.Function;

import java.util.*;

public class Tree {

    private List<Node> nodes = new LinkedList<>();

    private Node root = null;

    /**
     * 构建树形数据结构
     * @param functions 菜单功能列表
     */
    public Tree(List<Function> functions){
        for (Function function : functions) {
            NodeAttribute attr = new NodeAttribute(null == function.getUrl()?"": function.getUrl(),
                    function.getId());
            Node node = new Node(function.getId(),function.getParentId(),
                    function.getName(),"open",attr,function.getSerialNum());
            nodes.add(node);

            if(node.getId() == 0){
                root = node;
            }
        }
    }

    public List<Node> build(){
        buildTree(root);
        List<Node> result= new ArrayList<Node>();
        result.add(root);
        return result;
    }

    private void buildTree(Node parent){
        Node node = null;
        for(Iterator<Node> ite = nodes.iterator(); ite.hasNext();){
            node = ite.next();
            if(Objects.equals(node.getParentId(), parent.getId())){
                parent.getChildren().add(node);
                buildTree(node);
            }
        }
    }
}