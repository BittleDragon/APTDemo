package com.rxt.anolib;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Desc:注解信息及类代码生成
 *
 * @author raoxuting
 * since 2019/1/31
 */
public class ClassInfoBeanGenerator {

    private List<Integer> viewIds = new ArrayList<>();
    private final ClassName activityTypeName;
    private final ClassName viewTypeName;
    private final String methodName;
    private final String packageName;

    public ClassInfoBeanGenerator(ExecutableElement methodElement, Elements elementUtils) {
        PackageElement packageElement = elementUtils.getPackageOf(methodElement);
        packageName = packageElement.getQualifiedName().toString();
        methodName = methodElement.getSimpleName().toString();
        TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();
        activityTypeName = ClassName.get(typeElement);
        viewTypeName = ClassName.get("android.view", "View");
    }

    public String getPackageName() {
        return packageName;
    }

    public void setViewIds(Integer[] ids) {
        viewIds = Arrays.asList(ids);
    }

    public TypeSpec generateCode(String className) {

        FieldSpec activityField = FieldSpec.builder(activityTypeName,
                "activity", Modifier.PRIVATE).build();

        MethodSpec.Builder onClickMethodSpecBuilder =
                getOnClickMethodBuilder(activityTypeName, viewTypeName);

        MethodSpec.Builder onClickMethodImplBuilder = getOnClickMethodImplBuilder();

        ClassName viewOnClickListenerClass =
                ClassName.get(viewTypeName.toString(), "OnClickListener");

        return TypeSpec.classBuilder(className)
                .addSuperinterface(viewOnClickListenerClass)
                .addField(activityField)
                .addMethod(onClickMethodSpecBuilder.build())
                .addMethod(onClickMethodImplBuilder.build())
                .build();
    }

    private MethodSpec.Builder getOnClickMethodBuilder(ClassName activityTypeName, ClassName viewTypeName) {
        MethodSpec.Builder onClickMethodSpecBuilder = MethodSpec.methodBuilder("setOnClickListenerForView")
                .addModifiers(Modifier.PRIVATE)
                .returns(void.class)
                .addParameter(activityTypeName, "activity")
                .addStatement("this.activity = activity");

        if (viewIds != null && viewIds.size() != 0) {
            for (Integer viewId : viewIds) {
                onClickMethodSpecBuilder.addStatement("$T view_$L = $L.findViewById($L)",
                        viewTypeName, viewId, "activity", viewId);
                onClickMethodSpecBuilder.addStatement("view_$L.setOnClickListener(this)");
            }
        }
        return onClickMethodSpecBuilder;
    }

    private MethodSpec.Builder getOnClickMethodImplBuilder() {
        return MethodSpec.methodBuilder("onClick")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(viewTypeName, "view")
                .beginControlFlow("if (activity != null)")
                .addStatement("activity.$L(view)", methodName)
                .endControlFlow();
    }
}
