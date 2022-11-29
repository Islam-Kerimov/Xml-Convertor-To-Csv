package ru.develonica.model;

/**
 * Типы атрибутов.
 */
public enum ObjectAttribute {
    ID("ID"),
    OBJECT_ID("OBJECTID"),
    OBJECT_GU_ID("OBJECTGUID"),
    CHANGE_ID("CHANGEID"),
    NAME("NAME"),
    TYPE_NAME("TYPENAME"),
    LEVEL("LEVEL"),
    OPER_TYPE_ID("OPERTYPEID"),
    PREV_ID("PREVID"),
    NEXT_ID("NEXTID"),
    UPDATE_DATE("UPDATEDATE"),
    START_DATE("STARTDATE"),
    END_DATE("ENDDATE"),
    IS_ACTUAL("ISACTUAL"),
    IS_ACTIVE("ISACTIVE");

    private final String nodeName;

    ObjectAttribute(final String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }
}
