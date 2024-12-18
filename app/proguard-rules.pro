# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.google.android.filament.Box
-dontwarn com.google.android.filament.Camera
-dontwarn com.google.android.filament.Colors$RgbType
-dontwarn com.google.android.filament.Colors
-dontwarn com.google.android.filament.Engine
-dontwarn com.google.android.filament.Entity
-dontwarn com.google.android.filament.EntityManager
-dontwarn com.google.android.filament.Fence
-dontwarn com.google.android.filament.Filament
-dontwarn com.google.android.filament.IndexBuffer$Builder$IndexType
-dontwarn com.google.android.filament.IndexBuffer$Builder
-dontwarn com.google.android.filament.IndexBuffer
-dontwarn com.google.android.filament.IndirectLight$Builder
-dontwarn com.google.android.filament.IndirectLight
-dontwarn com.google.android.filament.LightManager$Builder
-dontwarn com.google.android.filament.LightManager$Type
-dontwarn com.google.android.filament.LightManager
-dontwarn com.google.android.filament.Material$Builder
-dontwarn com.google.android.filament.Material
-dontwarn com.google.android.filament.MaterialInstance
-dontwarn com.google.android.filament.NativeSurface
-dontwarn com.google.android.filament.RenderableManager$Builder
-dontwarn com.google.android.filament.RenderableManager$PrimitiveType
-dontwarn com.google.android.filament.RenderableManager
-dontwarn com.google.android.filament.Renderer
-dontwarn com.google.android.filament.Scene
-dontwarn com.google.android.filament.Skybox
-dontwarn com.google.android.filament.Stream$Builder
-dontwarn com.google.android.filament.Stream
-dontwarn com.google.android.filament.SwapChain
-dontwarn com.google.android.filament.Texture$Builder
-dontwarn com.google.android.filament.Texture$Format
-dontwarn com.google.android.filament.Texture$InternalFormat
-dontwarn com.google.android.filament.Texture$PixelBufferDescriptor
-dontwarn com.google.android.filament.Texture$PrefilterOptions
-dontwarn com.google.android.filament.Texture$Sampler
-dontwarn com.google.android.filament.Texture$Type
-dontwarn com.google.android.filament.Texture
-dontwarn com.google.android.filament.TextureSampler$MagFilter
-dontwarn com.google.android.filament.TextureSampler$MinFilter
-dontwarn com.google.android.filament.TextureSampler$WrapMode
-dontwarn com.google.android.filament.TextureSampler
-dontwarn com.google.android.filament.TransformManager
-dontwarn com.google.android.filament.VertexBuffer$AttributeType
-dontwarn com.google.android.filament.VertexBuffer$Builder
-dontwarn com.google.android.filament.VertexBuffer$VertexAttribute
-dontwarn com.google.android.filament.VertexBuffer
-dontwarn com.google.android.filament.View$AntiAliasing
-dontwarn com.google.android.filament.View$Dithering
-dontwarn com.google.android.filament.View$DynamicResolutionOptions
-dontwarn com.google.android.filament.View$RenderQuality
-dontwarn com.google.android.filament.View
-dontwarn com.google.android.filament.Viewport
-dontwarn com.google.android.filament.android.TextureHelper
-dontwarn com.google.android.filament.android.UiHelper$ContextErrorPolicy
-dontwarn com.google.android.filament.android.UiHelper$RendererCallback
-dontwarn com.google.android.filament.android.UiHelper
-dontwarn com.google.ar.sceneform.assets.Loader
-dontwarn com.google.ar.sceneform.assets.ModelData
-dontwarn com.google.ar.sceneform.collision.Box
-dontwarn com.google.ar.sceneform.collision.CollisionShape
-dontwarn com.google.ar.sceneform.collision.Sphere
-dontwarn com.google.ar.sceneform.common.TransformProvider
-dontwarn com.google.ar.sceneform.math.MathHelper
-dontwarn com.google.ar.sceneform.math.Matrix
-dontwarn com.google.ar.sceneform.math.Quaternion
-dontwarn com.google.ar.sceneform.math.Vector3
-dontwarn com.google.ar.sceneform.resources.ResourceHolder
-dontwarn com.google.ar.sceneform.resources.ResourceRegistry
-dontwarn com.google.ar.sceneform.resources.SharedReference
-dontwarn com.google.ar.sceneform.utilities.AndroidPreconditions
-dontwarn com.google.ar.sceneform.utilities.ChangeId
-dontwarn com.google.ar.sceneform.utilities.EnvironmentalHdrParameters
-dontwarn com.google.ar.sceneform.utilities.LoadHelper
-dontwarn com.google.ar.sceneform.utilities.Preconditions
-dontwarn com.google.ar.sceneform.utilities.SceneformBufferUtils
-dontwarn com.google.ar.schemas.lull.AabbDef
-dontwarn com.google.ar.schemas.lull.Mat4x3
-dontwarn com.google.ar.schemas.lull.ModelDef
-dontwarn com.google.ar.schemas.lull.ModelIndexRange
-dontwarn com.google.ar.schemas.lull.ModelInstanceDef
-dontwarn com.google.ar.schemas.lull.SkeletonDef
-dontwarn com.google.ar.schemas.lull.TextureDef
-dontwarn com.google.ar.schemas.lull.Vec3
-dontwarn com.google.ar.schemas.lull.Vec4
-dontwarn com.google.ar.schemas.lull.VertexAttribute
-dontwarn com.google.ar.schemas.sceneform.AnimationDef
-dontwarn com.google.ar.schemas.sceneform.BoolInit
-dontwarn com.google.ar.schemas.sceneform.BoolVec2Init
-dontwarn com.google.ar.schemas.sceneform.BoolVec3Init
-dontwarn com.google.ar.schemas.sceneform.BoolVec4Init
-dontwarn com.google.ar.schemas.sceneform.CompiledMaterialDef
-dontwarn com.google.ar.schemas.sceneform.IntInit
-dontwarn com.google.ar.schemas.sceneform.IntVec2Init
-dontwarn com.google.ar.schemas.sceneform.IntVec3Init
-dontwarn com.google.ar.schemas.sceneform.IntVec4Init
-dontwarn com.google.ar.schemas.sceneform.LightingCubeDef
-dontwarn com.google.ar.schemas.sceneform.LightingCubeFaceDef
-dontwarn com.google.ar.schemas.sceneform.LightingDef
-dontwarn com.google.ar.schemas.sceneform.MaterialDef
-dontwarn com.google.ar.schemas.sceneform.ParameterDef
-dontwarn com.google.ar.schemas.sceneform.ParameterInitDef
-dontwarn com.google.ar.schemas.sceneform.SamplerDef
-dontwarn com.google.ar.schemas.sceneform.SamplerInit
-dontwarn com.google.ar.schemas.sceneform.SamplerParamsDef
-dontwarn com.google.ar.schemas.sceneform.ScalarInit
-dontwarn com.google.ar.schemas.sceneform.SceneformBundleDef
-dontwarn com.google.ar.schemas.sceneform.SuggestedCollisionShapeDef
-dontwarn com.google.ar.schemas.sceneform.TransformDef
-dontwarn com.google.ar.schemas.sceneform.Vec2Init
-dontwarn com.google.ar.schemas.sceneform.Vec3Init
-dontwarn com.google.ar.schemas.sceneform.Vec4Init
-dontwarn com.google.ar.schemas.sceneform.VersionDef
-dontwarn com.google.flatbuffers.Table
-dontwarn java.awt.Component
-dontwarn java.awt.GraphicsEnvironment
-dontwarn java.awt.HeadlessException
-dontwarn java.awt.Window

-keep class com.asu1.quizzer.model.** { *; }
-keep interface com.asu1.quizzer.* { *; }
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class * extends android.app.Activity { *; }
-keep class * extends android.app.Service { *; }
-keep class * extends android.content.BroadcastReceiver { *; }
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends android.view.View { *; }
-keep @com.google.gson.annotations.SerializedName class * { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.asu1.quizzer.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.moshi.** { *; }
-keep class androidx.** { *; }
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}
# Keep CredentialManager and related classes
-keep class androidx.credentials.** { *; }
-keep class android.credentials.** { *; }
-keep class androidx.credentials.exceptions.** { *; }
-keep class androidx.credentials.CredentialProviderFrameworkImpl { *; }
-keep class androidx.credentials.CredentialProviderFrameworkImpl$* { *; }
-keep class com.google.googlesignin.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }

# Keep Compose Foundation LazyItemScope and related methods
-keep class androidx.compose.foundation.lazy.** { *; }

# Keep Compose UI Modifier and related methods
-keep class androidx.compose.ui.** { *; }

# Keep Compose Animation Core FiniteAnimationSpec and related methods
-keep class androidx.compose.animation.core.** { *; }
-keep class androidx.compose.animation.** { *; }

# keep imported library
-keep class sh.calvin.reorderable.** { *; }