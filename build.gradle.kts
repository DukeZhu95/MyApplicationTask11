// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
}

val roomVersion by extra("1.1.1")
val archLifecycleVersion by extra("1.1.1")