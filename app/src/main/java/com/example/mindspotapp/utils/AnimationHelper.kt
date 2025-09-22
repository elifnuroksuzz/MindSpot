package com.example.mindspotapp.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Premium animasyon efektleri için helper sınıf
 */
object AnimationHelper {

    /**
     * Scale animasyonu için tween ayarları
     */
    val scaleAnimationSpec = tween<Float>(
        durationMillis = Constants.ANIMATION_DURATION_SHORT,
        easing = EaseInOutCubic
    )

    /**
     * Fade animasyonu için tween ayarları
     */
    val fadeAnimationSpec = tween<Float>(
        durationMillis = Constants.ANIMATION_DURATION_MEDIUM,
        easing = EaseInOutCubic
    )

    /**
     * Slide animasyonu için tween ayarları
     */
    val slideAnimationSpec = tween<IntOffset>(
        durationMillis = Constants.ANIMATION_DURATION_MEDIUM,
        easing = EaseInOutQuart
    )

    /**
     * Bounce animasyonu için spring ayarları
     */
    val bounceAnimationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    /**
     * Pulse animasyonu - sürekli tekrar eden
     */
    @Composable
    fun infinitePulseAnimation(): Float {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        return infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        ).value
    }

    /**
     * Shimmer animasyonu - loading için
     */
    @Composable
    fun shimmerAnimation(): Float {
        val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
        return infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer_progress"
        ).value
    }
}

/**
 * Yumuşak scale geçişi için modifier
 */
@Composable
fun Modifier.animatedScale(isVisible: Boolean, delay: Int = 0): Modifier {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = Constants.ANIMATION_DURATION_MEDIUM,
            delayMillis = delay,
            easing = EaseOutBack
        ),
        label = "scale_animation"
    )

    return this.scale(scale)
}

/**
 * Fade geçişi için modifier
 */
@Composable
fun Modifier.animatedAlpha(isVisible: Boolean, delay: Int = 0): Modifier {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = Constants.ANIMATION_DURATION_MEDIUM,
            delayMillis = delay,
            easing = EaseInOutCubic
        ),
        label = "alpha_animation"
    )

    return this.graphicsLayer { this.alpha = alpha }
}

/**
 * Success animasyonu - checkmark ile
 */
@Composable
fun SuccessAnimation(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "success_scale"
    )

    val rotation by animateFloatAsState(
        targetValue = if (isVisible) 0f else -180f,
        animationSpec = tween(
            durationMillis = Constants.ANIMATION_DURATION_LONG,
            easing = EaseOutBack
        ),
        label = "success_rotation"
    )

    if (isVisible) {
        Card(
            modifier = modifier
                .size(60.dp)
                .scale(scale)
                .graphicsLayer {
                    rotationZ = rotation
                },
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50)
            )
        ) {
            // Checkmark icon burada olacak
        }
    }
}

/**
 * Content transition animasyonu
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedContent(
    targetState: Any,
    modifier: Modifier = Modifier,
    content: @Composable (Any) -> Unit
) {
    androidx.compose.animation.AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            slideInVertically(
                initialOffsetY = { height -> height },
                animationSpec = AnimationHelper.slideAnimationSpec
            ) with slideOutVertically(
                targetOffsetY = { height -> -height },
                animationSpec = AnimationHelper.slideAnimationSpec
            )
        },
        label = "animated_content"
    ) { state ->
        content(state)
    }
}