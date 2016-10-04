package me.icymint.sage.base.data.handler;

import me.icymint.sage.base.util.Jsons;
import me.icymint.sage.user.spec.entity.IdentityExtension;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by daniel on 2016/10/3.
 */
@MappedTypes(IdentityExtension.class)
public class IdentityExtensionTypeHandler extends BaseTypeHandler<IdentityExtension> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IdentityExtension parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Jsons.toJson(parameter));
    }

    @Override
    public IdentityExtension getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value != null) {
            return Jsons.fromJson(value, IdentityExtension.class);
        }
        return null;
    }

    @Override
    public IdentityExtension getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value != null) {
            return Jsons.fromJson(value, IdentityExtension.class);
        }
        return null;
    }

    @Override
    public IdentityExtension getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value != null) {
            return Jsons.fromJson(value, IdentityExtension.class);
        }
        return null;
    }
}
