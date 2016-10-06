package me.icymint.sage.user.rest.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by daniel on 2016/10/4.
 */
@Component
public class TokenThymeleafDialect extends AbstractDialect implements IProcessorDialect, IExpressionObjectDialect {
    public static final String NAME = "SageTokenStandard";
    public static final String DEFAULT_PREFIX = "token";
    public static final int PROCESSOR_PRECEDENCE = 800;
    @Autowired
    TokenExpressionObjectFactory tokenExpressObjectFactory;

    public TokenThymeleafDialect() {
        super(NAME);
    }

    public String getPrefix() {
        return DEFAULT_PREFIX;
    }


    public int getDialectProcessorPrecedence() {
        return PROCESSOR_PRECEDENCE;
    }

    public Set<IProcessor> getProcessors(final String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
        return processors;
    }


    public IExpressionObjectFactory getExpressionObjectFactory() {
        return tokenExpressObjectFactory;
    }
}
