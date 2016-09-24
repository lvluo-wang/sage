package me.icymint.sage.base.data.provider;

import org.apache.ibatis.jdbc.AbstractSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by daniel on 16/9/5.
 */
public class SQL extends AbstractSQL<SQL> {
    private final Logger logger = LoggerFactory.getLogger(SQL.class);


    @Override
    public SQL getSelf() {
        return this;
    }

    public SQL VALUES_IF(String columns, String values, boolean ifExists) {
        if (ifExists) {
            super.VALUES(columns, values);
        }
        return this;
    }

    public SQL VALUES_IF(String columns, String values, boolean ifExists, String otherwise) {
        if (ifExists) {
            super.VALUES(columns, values);
        } else {
            super.VALUES(columns, otherwise);
        }
        return this;
    }


    public SQL WHERE_IF(String conditions, boolean ifExists) {
        if (ifExists) {
            super.WHERE(conditions);
        }
        return this;
    }

    public SQL SET_IF(String sets, boolean ifExists, String otherwise) {
        if (ifExists) {
            SET(sets);
        } else if (!StringUtils.isEmpty(otherwise)) {
            SET(otherwise);
        }
        return this;
    }

    public SQL SET_IF(String sets, boolean ifExists) {
        return SET_IF(sets, ifExists, null);
    }

    public SQL log() {
        logger.info("SQL - " + this.toString());
        return this;
    }
}
