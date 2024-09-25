/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.getstream.log.sample.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
  background = background,
  primary = purple200,
  primaryVariant = purple500,
  secondary = purple500,
  onPrimary = Color.White,
  onSecondary = Color.White
)

private val LightColorPalette = lightColors(
  background = background,
  surface = Color.White,
  primary = purple200,
  primaryVariant = purple500,
  secondary = purple500,
  onPrimary = Color.White,
  onSecondary = Color.White
)

@Composable
fun SampleTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  val typography = if (darkTheme) {
    DarkTypography
  } else {
    LightTypography
  }

  MaterialTheme(
    colors = colors,
    typography = typography,
    content = content
  )
}
