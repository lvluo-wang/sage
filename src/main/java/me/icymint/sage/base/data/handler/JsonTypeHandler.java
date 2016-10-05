package me.icymint.sage.base.data.handler;

import me.icymint.sage.base.util.Jsons;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by daniel on 2016/10/3.
 */
public abstract class JsonTypeHandler<T> extends BaseTypeHandler<T> {


    protected abstract Class<T> getJsonClass();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Jsons.toJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value != null) {
            return Jsons.fromJson(value, getJsonClass());
        }
        return null;
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value != null) {
            return Jsons.fromJson(value, getJsonClass());
        }
        return null;
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value != null) {
            return Jsons.fromJson(value, getJsonClass());
        }
        return null;
    }
}
