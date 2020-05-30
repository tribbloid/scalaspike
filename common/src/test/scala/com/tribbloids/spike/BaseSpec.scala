package com.tribbloids.spike

import org.scalatest.FunSpec

//TODO: this class is useless as JUnitRunner superseded by scalatest-junit-runner plugin
// WARNING: cannot be a trait! or Java annotation won't work on subclasses
//@RunWith(classOf[JUnitRunner])
abstract class BaseSpec extends FunSpec {}
