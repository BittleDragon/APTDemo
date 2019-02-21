package com.rxt.anolib;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Desc:注解处理器
 *
 * @author raoxuting
 * since 2019/1/31
 */
@AutoService(Processor.class)
public class OnClickAnnoProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Map<String, ClassInfoBeanGenerator> classInfoMap = new HashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypes = new HashSet<>();
        supportedTypes.add(OnClick.class.getCanonicalName());
        return supportedTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(OnClick.class);
        for (Element element : elements) {
            ExecutableElement methodElement = (ExecutableElement) element;
            TypeElement classElement = (TypeElement) methodElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            ClassInfoBeanGenerator beanGenerator = classInfoMap.get(fullClassName);
            if (beanGenerator == null) {
                beanGenerator = new ClassInfoBeanGenerator(methodElement, elementUtils);
                classInfoMap.put(fullClassName, beanGenerator);
            }
            final OnClick onClickAnnotation = methodElement.getAnnotation(OnClick.class);
            final int[] array = onClickAnnotation.value();
            Integer[] value = Arrays.stream(array).boxed().toArray(Integer[]::new);
            beanGenerator.setViewIds(value);
        }
        for (String key : classInfoMap.keySet()) {
            ClassInfoBeanGenerator generator = classInfoMap.get(key);
            JavaFile javaFile = JavaFile.builder(generator.getPackageName(),
                    generator.generateCode(key)).build();
            try {
                //生成文件
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
