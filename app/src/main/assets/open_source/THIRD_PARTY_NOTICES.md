# 第三方许可与归属

根 LICENSE 为整个衍生项目的上游 AGPL-3.0 许可。原第三方组件按其各自许可分发；未把这些组件重新归属于当前维护者。

- 内含 Sketch 代码：Peng fei Pan，Apache-2.0；保留所有文件头。
- mojito：mikaelzero，Apache-2.0；保留上游本地模块与署名。
- PhotoView 等源文件：Chris Banes；SmoothInputLayout：AlexMofer；NineGridImageView：Plain；其他原代码声明以文件头为准。
- 内含的 Android / 图像编辑辅助代码原版权声明也保留，完整去重副本见 APK 和源码中的 `app/src/main/assets/open_source/THIRD_PARTY_SOURCE_NOTICES.txt`。
- jBCrypt：Damien Miller，ISC；将原 UI 错标的 Apache-2.0 更正为 ISC。

运行依赖清单由本次 releaseRuntimeClasspath 实际解析结果及对应 Maven POM 生成；缺少直接许可声明的模块从父 POM / 官方项目许可 / 原始源文件补足。许可证文本随源码和 APK 一起提供，位于 `app/src/main/assets/open_source/`。

| 依赖坐标 | POM / 原项目声明的许可证 | 项目 / 许可依据 |
|---|---|---|
| androidx.activity:activity:1.9.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/activity#1.9.2 |
| androidx.activity:activity-compose:1.9.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/activity#1.9.2 |
| androidx.activity:activity-ktx:1.9.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/activity#1.9.2 |
| androidx.annotation:annotation:1.9.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/annotation#1.9.1 |
| androidx.annotation:annotation-experimental:1.5.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/annotation#1.5.0 |
| androidx.annotation:annotation-jvm:1.9.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/annotation#1.9.1 |
| androidx.appcompat:appcompat:1.7.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/appcompat#1.7.0 |
| androidx.appcompat:appcompat-resources:1.7.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/appcompat#1.7.0 |
| androidx.arch.core:core-common:2.2.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/arch-core#2.2.0 |
| androidx.arch.core:core-runtime:2.2.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/arch-core#2.2.0 |
| androidx.autofill:autofill:1.0.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx |
| androidx.cardview:cardview:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.collection:collection:1.5.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/collection#1.5.0 |
| androidx.collection:collection-jvm:1.5.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/collection#1.5.0 |
| androidx.collection:collection-ktx:1.5.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/collection#1.5.0 |
| androidx.compose:compose-bom:2024.09.01 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack |
| androidx.compose.animation:animation:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-animation#1.7.1 |
| androidx.compose.animation:animation-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-animation#1.7.1 |
| androidx.compose.animation:animation-core:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-animation#1.7.1 |
| androidx.compose.animation:animation-core-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-animation#1.7.1 |
| androidx.compose.foundation:foundation:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-foundation#1.7.1 |
| androidx.compose.foundation:foundation-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-foundation#1.7.1 |
| androidx.compose.foundation:foundation-layout:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-foundation#1.7.1 |
| androidx.compose.foundation:foundation-layout-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-foundation#1.7.1 |
| androidx.compose.material:material:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material:material-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material:material-icons-core:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material:material-icons-core-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material:material-icons-extended:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material:material-icons-extended-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material:material-ripple:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material:material-ripple-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material#1.7.1 |
| androidx.compose.material3:material3:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material3#1.3.0 |
| androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material3#1.3.0 |
| androidx.compose.material3:material3-adaptive-navigation-suite-android:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material3#1.3.0 |
| androidx.compose.material3:material3-android:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material3#1.3.0 |
| androidx.compose.material3:material3-window-size-class-android:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material3#1.3.0 |
| androidx.compose.material3.adaptive:adaptive:1.0.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive#1.0.0 |
| androidx.compose.material3.adaptive:adaptive-android:1.0.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive#1.0.0 |
| androidx.compose.runtime:runtime:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-runtime#1.7.1 |
| androidx.compose.runtime:runtime-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-runtime#1.7.1 |
| androidx.compose.runtime:runtime-saveable:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-runtime#1.7.1 |
| androidx.compose.runtime:runtime-saveable-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-runtime#1.7.1 |
| androidx.compose.ui:ui:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-geometry:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-geometry-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-graphics:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-graphics-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-text:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-text-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-tooling:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-tooling-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-tooling-data:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-tooling-data-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-tooling-preview:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-tooling-preview-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-unit:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-unit-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-util:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.compose.ui:ui-util-android:1.7.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/compose-ui#1.7.1 |
| androidx.concurrent:concurrent-futures:1.1.0 | The Apache Software License, Version 2.0 | https://developer.android.com/topic/libraries/architecture/index.html |
| androidx.constraintlayout:constraintlayout:2.0.1 | The Apache Software License, Version 2.0 | http://tools.android.com |
| androidx.constraintlayout:constraintlayout-compose:1.1.0-beta01 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/constraintlayout#1.1.0-beta01 |
| androidx.constraintlayout:constraintlayout-compose-android:1.1.0-beta01 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/constraintlayout#1.1.0-beta01 |
| androidx.constraintlayout:constraintlayout-core:1.1.0-beta01 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/constraintlayout#1.1.0-beta01 |
| androidx.constraintlayout:constraintlayout-solver:2.0.1 | The Apache Software License, Version 2.0 | http://tools.android.com |
| androidx.coordinatorlayout:coordinatorlayout:1.1.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx |
| androidx.core:core:1.13.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/core#1.13.1 |
| androidx.core:core-ktx:1.13.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/core#1.13.1 |
| androidx.cursoradapter:cursoradapter:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.customview:customview:1.1.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx |
| androidx.customview:customview-poolingcontainer:1.0.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/customview#1.0.0 |
| androidx.databinding:viewbinding:9.0.0 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.datastore:datastore:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/datastore#1.1.1 |
| androidx.datastore:datastore-android:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/datastore#1.1.1 |
| androidx.datastore:datastore-core:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/datastore#1.1.1 |
| androidx.datastore:datastore-core-android:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/datastore#1.1.1 |
| androidx.datastore:datastore-core-okio:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/datastore#1.1.1 |
| androidx.datastore:datastore-core-okio-jvm:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/datastore#1.1.1 |
| androidx.documentfile:documentfile:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.drawerlayout:drawerlayout:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx |
| androidx.dynamicanimation:dynamicanimation:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.emoji2:emoji2:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/emoji2#1.3.0 |
| androidx.emoji2:emoji2-views-helper:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/emoji2#1.3.0 |
| androidx.exifinterface:exifinterface:1.3.7 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/exifinterface#1.3.7 |
| androidx.fragment:fragment:1.5.4 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/fragment#1.5.4 |
| androidx.graphics:graphics-path:1.0.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/graphics#1.0.1 |
| androidx.hilt:hilt-navigation:1.2.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/hilt#1.2.0 |
| androidx.hilt:hilt-navigation-compose:1.2.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/hilt#1.2.0 |
| androidx.interpolator:interpolator:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.legacy:legacy-support-core-utils:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.lifecycle:lifecycle-common:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-common-java8:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-common-jvm:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-livedata:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-livedata-core:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-livedata-core-ktx:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-livedata-ktx:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-process:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-runtime:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-runtime-android:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-runtime-compose:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-runtime-compose-android:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-runtime-ktx:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-runtime-ktx-android:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-viewmodel:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-viewmodel-android:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.5 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/lifecycle#2.8.5 |
| androidx.loader:loader:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.localbroadcastmanager:localbroadcastmanager:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.media3:media3-common:1.7.1 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.media3:media3-container:1.7.1 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.media3:media3-database:1.7.1 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.media3:media3-datasource:1.7.1 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.media3:media3-decoder:1.7.1 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.media3:media3-exoplayer:1.7.1 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.media3:media3-extractor:1.7.1 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| androidx.navigation:navigation-common:2.8.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/navigation#2.8.0 |
| androidx.navigation:navigation-common-ktx:2.8.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/navigation#2.8.0 |
| androidx.navigation:navigation-compose:2.8.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/navigation#2.8.0 |
| androidx.navigation:navigation-runtime:2.8.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/navigation#2.8.0 |
| androidx.navigation:navigation-runtime-ktx:2.8.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/navigation#2.8.0 |
| androidx.print:print:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.profileinstaller:profileinstaller:1.3.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/profileinstaller#1.3.1 |
| androidx.recyclerview:recyclerview:1.3.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/recyclerview#1.3.2 |
| androidx.resourceinspection:resourceinspection-annotation:1.0.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/resourceinspection#1.0.1 |
| androidx.room:room-common:2.8.4 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/room#2.8.4 |
| androidx.room:room-common-jvm:2.8.4 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/room#2.8.4 |
| androidx.room:room-ktx:2.8.4 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/room#2.8.4 |
| androidx.room:room-runtime:2.8.4 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/room#2.8.4 |
| androidx.room:room-runtime-android:2.8.4 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/room#2.8.4 |
| androidx.savedstate:savedstate:1.2.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/savedstate#1.2.1 |
| androidx.savedstate:savedstate-ktx:1.2.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/savedstate#1.2.1 |
| androidx.sqlite:sqlite:2.6.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/sqlite#2.6.2 |
| androidx.sqlite:sqlite-android:2.6.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/sqlite#2.6.2 |
| androidx.sqlite:sqlite-framework:2.6.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/sqlite#2.6.2 |
| androidx.sqlite:sqlite-framework-android:2.6.2 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/sqlite#2.6.2 |
| androidx.startup:startup-runtime:1.1.1 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/startup#1.1.1 |
| androidx.tracing:tracing:1.0.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/tracing#1.0.0 |
| androidx.transition:transition:1.5.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/transition#1.5.0 |
| androidx.vectordrawable:vectordrawable:1.1.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx |
| androidx.vectordrawable:vectordrawable-animated:1.1.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx |
| androidx.versionedparcelable:versionedparcelable:1.1.1 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.viewpager:viewpager:1.0.0 | The Apache Software License, Version 2.0 | http://developer.android.com/tools/extras/support-library.html |
| androidx.viewpager2:viewpager2:1.1.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/viewpager2#1.1.0 |
| androidx.webkit:webkit:1.11.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/webkit#1.11.0 |
| androidx.window:window:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/window#1.3.0 |
| androidx.window:window-core:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/window#1.3.0 |
| androidx.window:window-core-android:1.3.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/window#1.3.0 |
| androidx.window.extensions.core:core:1.0.0 | The Apache Software License, Version 2.0 | https://developer.android.com/jetpack/androidx/releases/window-extensions-core#1.0.0 |
| com.aliyun.dpa:oss-android-sdk:2.9.19 | The Apache License, Version 2.0 | https://github.com/aliyun/aliyun-oss-android-sdk |
| com.caverock:androidsvg-aar:1.4 | The Apache Software License, Version 2.0 | https://github.com/BigBadaboom/androidsvg |
| com.geyifeng.immersionbar:immersionbar:3.2.2 | The Apache License, Version 2.0 | https://github.com/gyf-dev/ImmersionBar |
| com.github.MikaelZero.mojito:coilimageloader:1.8.7 | Apache-2.0 | https://github.com/mikaelzero/mojito |
| com.github.ajalt.colormath:colormath:3.5.0 | The MIT License | https://github.com/ajalt/colormath/ |
| com.github.ajalt.colormath:colormath-jvm:3.5.0 | The MIT License | https://github.com/ajalt/colormath/ |
| com.google.accompanist:accompanist-drawablepainter:0.36.0 | The Apache Software License, Version 2.0 | https://github.com/google/accompanist/ |
| com.google.android.material:material:1.12.0 | The Apache Software License, Version 2.0 | https://github.com/material-components/material-components-android |
| com.google.code.findbugs:jsr305:3.0.2 | The Apache Software License, Version 2.0 | http://findbugs.sourceforge.net/ |
| com.google.code.gson:gson:2.10.1 | Apache-2.0 | https://www.apache.org/licenses/LICENSE-2.0.txt |
| com.google.dagger:dagger:2.59 | Apache 2.0 | https://github.com/google/dagger |
| com.google.dagger:dagger-lint-aar:2.59 | Apache 2.0 | https://github.com/google/dagger |
| com.google.dagger:hilt-android:2.59 | Apache 2.0 | https://github.com/google/dagger |
| com.google.dagger:hilt-core:2.59 | Apache 2.0 | https://github.com/google/dagger |
| com.google.errorprone:error_prone_annotations:2.15.0 | Apache 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| com.google.guava:failureaccess:1.0.2 | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| com.google.guava:guava:33.3.1-android | Apache License, Version 2.0 | https://github.com/google/guava |
| com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava | The Apache Software License, Version 2.0 | http://www.apache.org/licenses/LICENSE-2.0.txt |
| com.google.protobuf:protobuf-javalite:4.28.1 | BSD-3-Clause | https://opensource.org/licenses/BSD-3-Clause |
| com.google.protobuf:protobuf-kotlin-lite:4.28.1 | BSD-3-Clause | https://opensource.org/licenses/BSD-3-Clause |
| com.materialkolor:material-color-utilities:1.7.0 | The MIT License | https://github.com/jordond/materialkolor |
| com.materialkolor:material-color-utilities-android:1.7.0 | The MIT License | https://github.com/jordond/materialkolor |
| com.materialkolor:material-kolor:1.7.0 | The MIT License | https://github.com/jordond/materialkolor |
| com.materialkolor:material-kolor-android:1.7.0 | The MIT License | https://github.com/jordond/materialkolor |
| com.squareup.okhttp3:logging-interceptor:4.12.0 | The Apache Software License, Version 2.0 | https://square.github.io/okhttp/ |
| com.squareup.okhttp3:okhttp:4.12.0 | The Apache Software License, Version 2.0 | https://square.github.io/okhttp/ |
| com.squareup.okio:okio:3.9.0 | The Apache Software License, Version 2.0 | https://github.com/square/okio/ |
| com.squareup.okio:okio-jvm:3.9.0 | The Apache Software License, Version 2.0 | https://github.com/square/okio/ |
| com.squareup.retrofit2:converter-gson:2.11.0 | The Apache Software License, Version 2.0 | https://github.com/square/retrofit |
| com.squareup.retrofit2:retrofit:2.11.0 | The Apache Software License, Version 2.0 | https://github.com/square/retrofit |
| dev.drewhamilton.poko:poko-annotations:0.16.0 | The Apache Software License, Version 2.0 | https://github.com/drewhamilton/Poko |
| dev.drewhamilton.poko:poko-annotations-jvm:0.16.0 | The Apache Software License, Version 2.0 | https://github.com/drewhamilton/Poko |
| io.coil-kt:coil:2.7.0 | The Apache License, Version 2.0 | https://github.com/coil-kt/coil |
| io.coil-kt:coil-base:2.7.0 | The Apache License, Version 2.0 | https://github.com/coil-kt/coil |
| io.coil-kt:coil-compose:2.7.0 | The Apache License, Version 2.0 | https://github.com/coil-kt/coil |
| io.coil-kt:coil-compose-base:2.7.0 | The Apache License, Version 2.0 | https://github.com/coil-kt/coil |
| io.coil-kt:coil-gif:2.7.0 | The Apache License, Version 2.0 | https://github.com/coil-kt/coil |
| io.coil-kt:coil-svg:2.1.0 | The Apache License, Version 2.0 | https://github.com/coil-kt/coil |
| io.github.panpf.sketch:sketch-gif:2.7.1 | The Apache Software License, Version 2.0 | https://github.com/panpf/sketch |
| jakarta.inject:jakarta.inject-api:2.0.1 | The Apache Software License, Version 2.0 | https://github.com/eclipse-ee4j/injection-api |
| javax.inject:javax.inject:1 | Apache-2.0 | https://repo.maven.apache.org/maven2/javax/inject/javax.inject/1/javax.inject-1-sources.jar |
| jp.wasabeef.transformers:coil:1.0.6 | The Apache Software License, Version 2.0 | https://github.com//wasabeef/transformers |
| jp.wasabeef.transformers:core:1.0.6 | The Apache Software License, Version 2.0 | https://github.com//wasabeef/transformers |
| jp.wasabeef.transformers:types:1.0.6 | The Apache Software License, Version 2.0 | https://github.com//wasabeef/transformers |
| me.onebone:toolbar-compose:2.3.5 | MIT License | https://github.com/onebone/compose-collapsing-toolbar |
| me.zhanghai.android.appiconloader:appiconloader:1.5.0 | The Apache Software License, Version 2.0 | https://github.com/zhanghai/AppIconLoader |
| me.zhanghai.android.appiconloader:appiconloader-coil:1.5.0 | The Apache Software License, Version 2.0 | https://github.com/zhanghai/AppIconLoader |
| me.zhanghai.android.appiconloader:appiconloader-iconloaderlib:1.5.0 | The Apache Software License, Version 2.0 | https://github.com/zhanghai/AppIconLoader |
| org.jetbrains:annotations:23.0.0 | The Apache Software License, Version 2.0 | https://github.com/JetBrains/java-annotations |
| org.jetbrains.compose.material3:material3:1.6.11 | The Apache Software License, Version 2.0 | https://github.com/JetBrains/compose-jb |
| org.jetbrains.compose.runtime:runtime:1.6.11 | The Apache Software License, Version 2.0 | https://github.com/JetBrains/compose-jb |
| org.jetbrains.compose.ui:ui:1.6.11 | The Apache Software License, Version 2.0 | https://github.com/JetBrains/compose-jb |
| org.jetbrains.kotlin:kotlin-android-extensions-runtime:2.2.10 | The Apache License, Version 2.0 | https://kotlinlang.org/ |
| org.jetbrains.kotlin:kotlin-bom:1.8.22 | The Apache Software License, Version 2.0 | https://kotlinlang.org/ |
| org.jetbrains.kotlin:kotlin-parcelize-runtime:2.2.10 | The Apache License, Version 2.0 | https://kotlinlang.org/ |
| org.jetbrains.kotlin:kotlin-stdlib:2.2.20 | Apache-2.0 | https://kotlinlang.org/ |
| org.jetbrains.kotlin:kotlin-stdlib-common:2.2.20 | Apache-2.0 | https://kotlinlang.org/ |
| org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.22 | The Apache License, Version 2.0 | https://kotlinlang.org/ |
| org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22 | The Apache License, Version 2.0 | https://kotlinlang.org/ |
| org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1 | The Apache Software License, Version 2.0 | https://github.com/Kotlin/kotlinx.coroutines |
| org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.1 | The Apache Software License, Version 2.0 | https://github.com/Kotlin/kotlinx.coroutines |
| org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1 | The Apache Software License, Version 2.0 | https://github.com/Kotlin/kotlinx.coroutines |
| org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1 | The Apache Software License, Version 2.0 | https://github.com/Kotlin/kotlinx.coroutines |
| org.jetbrains.kotlinx:kotlinx-serialization-bom:1.6.3 | The Apache Software License, Version 2.0 | https://github.com/Kotlin/kotlinx.serialization |
| org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3 | The Apache Software License, Version 2.0 | https://github.com/Kotlin/kotlinx.serialization |
| org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.6.3 | The Apache Software License, Version 2.0 | https://github.com/Kotlin/kotlinx.serialization |
| org.jsoup:jsoup:1.18.1 | The MIT License | https://jsoup.org/ |
| org.jspecify:jspecify:1.0.0 | The Apache License, Version 2.0 | http://jspecify.org/ |
| org.mindrot:jbcrypt:0.4 | ISC | https://github.com/djmdjm/jBCrypt |
| pl.droidsonroids:relinker:1.3.1 | Apache-v2 | https://github.com/koral--/ReLinker |
