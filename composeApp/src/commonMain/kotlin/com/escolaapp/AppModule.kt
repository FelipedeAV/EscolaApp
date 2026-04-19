package com.escolaapp

import com.escolaapp.core.coreModule
import com.escolaapp.features.auth.authModule
import com.escolaapp.features.coordinator.coordinatorModule
import com.escolaapp.features.guardian.guardianModule
import com.escolaapp.features.teacher.teacherModule

val appModule = listOf(
    coreModule,
    authModule,
    teacherModule,
    guardianModule,
    coordinatorModule
)