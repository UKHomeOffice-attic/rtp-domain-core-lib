package domain.core.mongo

import com.novus.salat.Context

package object salat {
  // TODO var???
  implicit var con: Context = createContextWithClassLoader(this.getClass.getClassLoader)

  def createContextWithClassLoader(classLoader: ClassLoader) = {
    val c = new Context {
      val name = "rtp-domain-core-context"
    }

    c.registerClassLoader(classLoader)

    con = c
    c
  }

  lazy implicit val ctx = con
}