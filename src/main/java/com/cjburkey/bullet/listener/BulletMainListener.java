package com.cjburkey.bullet.listener;

import com.cjburkey.bullet.Log;
import com.cjburkey.bullet.antlr.BulletBaseListener;
import com.cjburkey.bullet.antlr.BulletParser;
import com.cjburkey.bullet.obj.BExpression;
import com.cjburkey.bullet.obj.BFunction;
import com.cjburkey.bullet.obj.BProgram;
import com.cjburkey.bullet.obj.IBScope;
import com.cjburkey.bullet.obj.statement.BArgument;
import com.cjburkey.bullet.obj.statement.BExpressionStatement;
import com.cjburkey.bullet.obj.statement.BIfStatement;
import com.cjburkey.bullet.obj.statement.BStatement;
import com.cjburkey.bullet.obj.statement.BVariable;
import java.util.Stack;

/**
 * Created by CJ Burkey on 2018/11/03
 */
public class BulletMainListener extends BulletBaseListener {
    
    public static final Stack<BProgram> programs = new Stack<>();
    
    private BFunction currentFunction;
    private BVariable currentVariable;
    private Stack<IBScope> currentScope = new Stack<>();
    
    public void enterProgram(BulletParser.ProgramContext ctx) {
        BProgram program = new BProgram(ctx);
        currentScope.push(program);
        programs.push(program);
    }
    
    public void exitProgram(BulletParser.ProgramContext ctx) {
        currentScope.pop();
    }
    
    public void enterFunction(BulletParser.FunctionContext ctx) {
        currentFunction = new BFunction(ctx);
        currentScope.push(currentFunction);
        programs.peek().functions.add(currentFunction);
    }
    
    public void exitFunction(BulletParser.FunctionContext ctx) {
        currentFunction = null;
        currentScope.pop();
    }
    
    public void exitArgument(BulletParser.ArgumentContext ctx) {
        currentFunction.arguments.add(new BArgument(ctx));
    }
    
    public void enterVariableDef(BulletParser.VariableDefContext ctx) {
        currentVariable = new BVariable(currentScope.peek(), ctx);
    }
    
    public void exitVariableDef(BulletParser.VariableDefContext ctx) {
        addStatement(currentVariable);
        currentVariable = null;
    }
    
    public void exitVariableVal(BulletParser.VariableValContext ctx) {
        if (ctx.expression() != null) {
            currentVariable.value = new BExpression(ctx.expression());
        }
    }
    
    public void enterStatementIf(BulletParser.StatementIfContext ctx) {
        BIfStatement ifStatement = new BIfStatement(currentScope.peek(), ctx);
        addStatement(ifStatement);
        currentScope.push(ifStatement);
    }
    
    public void exitStatementIf(BulletParser.StatementIfContext ctx) {
        currentScope.pop();
    }
    
    public void exitStatementExpression(BulletParser.StatementExpressionContext ctx) {
        addStatement(new BExpressionStatement(currentScope.peek(), ctx.expression()));
    }
    
    private void addStatement(BStatement statement) {
        currentScope.peek().getStatements().add(statement);
    }
    
}
