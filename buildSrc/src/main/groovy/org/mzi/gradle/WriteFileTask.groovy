// Copyright (c) 2020-2020 Yinsen (Tesla) Zhang.
// Use of this source code is governed by the Apache-2.0 license that can be found in the LICENSE file.
package org.mzi.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory

class WriteFileTask extends DefaultTask {
  @OutputDirectory File outputDir
  @Input String className
  @Input def basePackage = project.group
}
