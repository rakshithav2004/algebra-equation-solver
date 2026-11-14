package com.example.algebra.model;

public class Equation {

    private String equationId;
    private TreeNode root;

    public Equation(String equationId, TreeNode root) {
        this.equationId = equationId;
        this.root = root;
    }

    public String getEquationId() {
        return equationId;
    }

    public TreeNode getRoot() {
        return root;
    }
}
