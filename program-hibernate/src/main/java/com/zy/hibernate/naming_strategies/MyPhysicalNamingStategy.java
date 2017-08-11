package com.zy.hibernate.naming_strategies;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.*;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/10.
 */
public class MyPhysicalNamingStategy implements PhysicalNamingStrategy {
    
    private static final Map<String ,String> ABBREVIATIONS = buildAbbreviationMap();

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("PhysicalNamingStrategy -> toPhysicalCatalogName : " + identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("PhysicalNamingStrategy -> toPhysicalSchemaName : " + identifier);
        return identifier;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("PhysicalNamingStrategy -> toPhysicalTableName : " + identifier);
        final List<String> parts = splitAndReplace(identifier.getText());
        return jdbcEnvironment.getIdentifierHelper()
                .toIdentifier(join(parts),identifier.isQuoted());
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("PhysicalNamingStrategy -> toPhysicalSequenceName : " + identifier);
        final LinkedList<String> parts = splitAndReplace(identifier.getText());
        // Acme Corp says all sequences should end with _seq
        if(!"seq".equalsIgnoreCase(parts.getLast())){
            parts.add("seq");
        }
        return jdbcEnvironment.getIdentifierHelper()
                .toIdentifier(join(parts),identifier.isQuoted());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        System.out.println("PhysicalNamingStrategy -> toPhysicalColumnName : " + identifier);
        final List<String> parts = splitAndReplace(identifier.getText());
        return jdbcEnvironment.getIdentifierHelper()
                .toIdentifier(join(parts),identifier.isQuoted());
    }


    private static Map<String,String> buildAbbreviationMap() {
        TreeMap<String,String> abbreviationMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        abbreviationMap.put("account","acct");
        abbreviationMap.put("number","num");
        return abbreviationMap;
    }

    private LinkedList<String> splitAndReplace(String name){
        LinkedList<String> result = new LinkedList<>();
        for(String part : StringUtils.splitByCharacterTypeCamelCase(name)){
            if(part == null || part.trim().isEmpty()){
                continue;
            }
            part = applyAbbreviationReplacement(part);
            result.add(part.toLowerCase(Locale.ROOT));
        }
        return result;
    }

    private String applyAbbreviationReplacement(String word) {
        if(ABBREVIATIONS.containsKey(word)){
            return ABBREVIATIONS.get(word);
        }
        return word;
    }

    private String join(List<String> parts){
        boolean firstPass = true;
        String separator = "";
        StringBuilder joined = new StringBuilder();
        for(String part : parts){
            joined.append(separator).append(part);
            if(firstPass){
                firstPass = false;
                separator = "_";
            }
        }
        return joined.toString();
    }
}
