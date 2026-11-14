package com.example.algebra.service;

import com.example.algebra.model.Equation;
import com.example.algebra.model.TreeNode;
import com.example.algebra.parser.ExpressionParser;
import com.example.algebra.repository.EquationRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
public class EquationService {

    private final EquationRepository equationRepository;
    private final ExpressionParser expressionParser;

    public EquationService(EquationRepository equationRepository) {
        this.equationRepository = equationRepository;
        this.expressionParser = new ExpressionParser();
    }

    public String storeEquation(String equationText) {

        var tokens = expressionParser.tokenize(equationText);

        var postfix = expressionParser.toPostfix(tokens);

        TreeNode root =
                expressionParser.buildExpressionTree(postfix);

        String equationId = UUID.randomUUID().toString();

        Equation equation = new Equation(equationId, root);

        equationRepository.save(equation);

        return equationId;
    }

    public Collection<Equation> getAllEquations() {
        return equationRepository.findAll();
    }

    public Equation getEquationById(String equationId) {

        return equationRepository.findById(equationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Equation not found: " + equationId
                        )
                );
    }

    public double evaluateEquation(
            String equationId,
            Map<String, Double> variables) {

        Equation equation = getEquationById(equationId);

        return expressionParser.evaluate(
                equation.getRoot(),
                variables
        );
    }

    public String reconstructEquation(TreeNode node) {

        if (node == null) {
            throw new IllegalArgumentException(
                    "Invalid expression tree"
            );
        }

        String value = node.getValue();

        if (value.equals("u-")) {
            return "-(" +
                    reconstructEquation(node.getLeft()) +
                    ")";
        }

        if (value.matches("[+\\-*/^]")) {

            String left = reconstructEquation(node.getLeft());
            String right = reconstructEquation(node.getRight());

            return "(" + left + " " + value + " " + right + ")";
        }

        return value;
    }
}
