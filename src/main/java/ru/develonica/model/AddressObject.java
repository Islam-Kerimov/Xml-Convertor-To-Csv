package ru.develonica.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static java.lang.String.format;

/**
 * POJO класс объекта <code>ADDRESSOBJECTS</code>.
 */
@Setter
@Getter
public class AddressObject {

    /**
     * Формат для записи в csv файл.
     */
    private static final String CSV_FILE_PATTERN = "%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s";

    /**
     * Показатель ID объекта.
     */
    private Long id;

    /**
     * Показатель OBJECTID объекта.
     */
    private Long objectId;

    /**
     * Показатель OBJECTGUID объекта.
     */
    private String objectGuId;

    /**
     * Показатель CHANGEID объекта.
     */
    private Long changeId;

    /**
     * Показатель NAME объекта.
     */
    private String name;

    /**
     * Показатель TYPENAME объекта.
     */
    private String typeName;

    /**
     * Показатель LEVEL объекта.
     */
    private Long level;

    /**
     * Показатель OPERTYPEID объекта.
     */
    private Long operTypeId;

    /**
     * Показатель PREVID объекта.
     */
    private Long prevId;

    /**
     * Показатель NEXTID объекта.
     */
    private Long nextId;

    /**
     * Показатель UPDATEDATE объекта.
     */
    private LocalDate updateDate;

    /**
     * Показатель STARTDATE объекта.
     */
    private LocalDate startDate;

    /**
     * Показатель ENDDATE объекта.
     */
    private LocalDate endDate;

    /**
     * Показатель ISACTUAL объекта.
     */
    private Integer isActual;

    /**
     * Показатель ISACTIVE объекта.
     */
    private Integer isActive;

    @Override
    public String toString() {
        return format(CSV_FILE_PATTERN,
                id, objectId, objectGuId, changeId, name, typeName, level, operTypeId,
                prevId, nextId, updateDate, startDate, endDate, isActual, isActive);
    }
}
