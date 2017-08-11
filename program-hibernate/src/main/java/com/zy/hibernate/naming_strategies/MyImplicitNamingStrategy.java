package com.zy.hibernate.naming_strategies;

import org.hibernate.boot.model.naming.*;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/10.
 */
public class MyImplicitNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl{
    private static final long serialVersionUID = 3658599279859431693L;

    @Override
    public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
        Identifier identifier = super.determinePrimaryTableName(source);
        Identifier result = toStandard(identifier,"Bean");
        System.out.println("ImplicitNamingStrategy / determinePrimaryTableName -> \n\t" + identifier + " => " + result);
        return result;
    }

    private Identifier toStandard(Identifier identifier, String... removeSuffixes) {
        if(removeSuffixes == null){
            return identifier;
        }
        if(identifier == null){
            return null;
        }
        String text = identifier.getText();
        if(removeSuffixes != null){
            for(String suffix : removeSuffixes){
                if(text.endsWith(suffix)){
                    text = text.substring(0, text.length() - suffix.length());
                }
            }
        }
        return new Identifier(text,identifier.isQuoted());
    }

    @Override
    public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
        Identifier identifier = super.determineJoinTableName(source);
        System.out.println("ImplicitNamingStrategy / JoinTableName -> \n\t" + identifier);
        return identifier;
    }

    @Override
    public Identifier determineCollectionTableName(ImplicitCollectionTableNameSource source) {
        Identifier name = super.determineCollectionTableName(source);
        System.out.println("ImplicitNamingStrategy / CollectionTableName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineDiscriminatorColumnName(ImplicitDiscriminatorColumnNameSource source) {
        Identifier name = super.determineDiscriminatorColumnName(source);
        System.out.println("ImplicitNamingStrategy / DiscriminatorColumnName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineTenantIdColumnName(ImplicitTenantIdColumnNameSource source) {
        Identifier name = super.determineTenantIdColumnName(source);
        System.out.println("ImplicitNamingStrategy / TenantIdColumnName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineIdentifierColumnName(ImplicitIdentifierColumnNameSource source) {
        Identifier name = super.determineIdentifierColumnName(source);
        System.out.println("ImplicitNamingStrategy / IdentifierColumnName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        Identifier name = super.determineBasicColumnName(source);
        System.out.println("ImplicitNamingStrategy / BasicColumnName -> \n\t" + name);
        return name;
    }

    @Override
    public Identifier determineJoinColumnName(ImplicitJoinColumnNameSource source) {
        Identifier name = super.determineJoinColumnName(source);
        final String result;
        if(source.getNature() == ImplicitJoinColumnNameSource.Nature.ELEMENT_COLLECTION){
            result = transformEntityName(source.getEntityNaming());
        }else{
            result = transformAttributePath(source.getAttributePath());
        }
        System.out.println("ImplicitNamingStrategy / JoinColumnName -> \n\t" + name + " => " + result);
        return toIdentifier( result, source.getBuildingContext() );
    }
    @Override
    public Identifier determinePrimaryKeyJoinColumnName(ImplicitPrimaryKeyJoinColumnNameSource source) {
        Identifier name = super.determinePrimaryKeyJoinColumnName(source);
        System.out.println("ImplicitNamingStrategy / PrimaryKeyJoinColumnName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineAnyDiscriminatorColumnName(ImplicitAnyDiscriminatorColumnNameSource source) {
        Identifier name = super.determineAnyDiscriminatorColumnName(source);
        System.out.println("ImplicitNamingStrategy / AnyDiscriminatorColumnName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineAnyKeyColumnName(ImplicitAnyKeyColumnNameSource source) {
        Identifier name = super.determineAnyKeyColumnName(source);
        System.out.println("ImplicitNamingStrategy / AnyKeyColumnName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineMapKeyColumnName(ImplicitMapKeyColumnNameSource source) {
        Identifier name = super.determineMapKeyColumnName(source);
        System.out.println("ImplicitNamingStrategy / MapKeyColumnName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineListIndexColumnName(ImplicitIndexColumnNameSource source) {
        Identifier name = super.determineListIndexColumnName(source);
        System.out.println("ImplicitNamingStrategy / ListIndexColumnName -> \n\t" + name);
        return name;
    }

    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        Identifier name = super.determineForeignKeyName(source);
        String result = null;
        String tableName = source.getTableName().getText();
        if(tableName.startsWith(TableNamingConfig.TABLE_PREFIX)){
            tableName = tableName.substring(TableNamingConfig.TABLE_PREFIX.length());
        }
        if(source.getColumnNames().size() == 1){
            result = TableNamingConfig.FOREIGN_KEY_PREFIX + tableName +"_"+source.getColumnNames().get(0).getText();
        }else {
            String columnName = source.getReferencedTableName().getText();
            if(columnName.startsWith(TableNamingConfig.TABLE_PREFIX)){
                columnName = columnName.substring(TableNamingConfig.TABLE_PREFIX.length());
            }
            result = TableNamingConfig.FOREIGN_KEY_PREFIX+tableName +"_"+columnName;
        }
        System.out.println("ImplicitNamingStrategy / ForeignKeyName -> \n\t" + name + " => " + result);
        return new Identifier(result, name.isQuoted());
    }
    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
        Identifier name = super.determineUniqueKeyName(source);
        System.out.println("ImplicitNamingStrategy / UniqueKeyName -> \n\t" + name);
        return name;
    }
    @Override
    public Identifier determineIndexName(ImplicitIndexNameSource source) {
        Identifier name = super.determineIndexName(source);
        System.out.println("ImplicitNamingStrategy / IndexName -> \n\t" + name);
        return name;
    }
}
