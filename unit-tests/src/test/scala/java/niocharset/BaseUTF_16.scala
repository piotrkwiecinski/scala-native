package java.niocharset

import java.nio.charset.Charset
import BaseCharset._

class BaseUTF_16(charset: Charset) extends BaseCharset(charset) {
  test("decode") {
    // ASCII characters
    testDecode(bb"0042 006f 006e 006a 006f 0075 0072")(cb"Bonjour")

    // Other characters without surrogate pairs
    testDecode(bb"0047 0072 00fc 00df 0020 0047 006f 0074 0074")(cb"Grüß Gott")
    testDecode(bb"039a 03b1 03bb 03b7 03bc 03ad 03c1 03b1")(cb"Καλημέρα")
    testDecode(bb"0635 0628 0627 062d 0020 0627 0644 062e 064a 0631")(cb"صباح الخير")
    testDecode(bb"3053 3093 306b 3061 306f")(cb"こんにちは")
    testDecode(bb"0414 043e 0431 0440 044b 0439 0020 0434 0435 043d 044c")(cb"Добрый день")
    testDecode(bb"4f60 597d")(cb"你好")

    // 4-byte characters
    testDecode(bb"d835 dcd7 d835 dcee d835 dcf5 d835 dcf5 d835 dcf8")(
      cb"\ud835\udcd7\ud835\udcee\ud835\udcf5\ud835\udcf5\ud835\udcf8")

    testDecode(bb"")(cb"")

    // Here begin the sequences with at least one error

    // Single UTF-16 surrogates
    testDecode(bb"d800")(Malformed(2))
    testDecode(bb"daff")(Malformed(2))
    testDecode(bb"db80")(Malformed(2))
    testDecode(bb"dbff")(Malformed(2))
    testDecode(bb"dc00")(Malformed(2))
    testDecode(bb"df80")(Malformed(2))
    testDecode(bb"dfff")(Malformed(2))

    // High UTF-16 surrogates not followed by low surrogates

    testDecode(bb"d800 0041")(Malformed(2), cb"A")
    testDecode(bb"d800 d800")(Malformed(2), Malformed(2))
    testDecode(bb"d800 d835 dcd7")(Malformed(2), cb"\ud835\udcd7")
    testDecode(bb"dbff 0041")(Malformed(2), cb"A")
    testDecode(bb"dbff db8f")(Malformed(2), Malformed(2))
    testDecode(bb"dbff d835 dcd7")(Malformed(2), cb"\ud835\udcd7")

    // Lonely byte at the end
    testDecode(bb"0041 41")(cb"A", Malformed(1))
  }

  test("encode") {
    // ASCII characters
    testEncode(cb"Bonjour")(bb"0042 006f 006e 006a 006f 0075 0072")

    // Other characters without surrogate pairs
    testEncode(cb"Grüß Gott")(bb"0047 0072 00fc 00df 0020 0047 006f 0074 0074")
    testEncode(cb"Καλημέρα")(bb"039a 03b1 03bb 03b7 03bc 03ad 03c1 03b1")
    testEncode(cb"صباح الخير")(bb"0635 0628 0627 062d 0020 0627 0644 062e 064a 0631")
    testEncode(cb"こんにちは")(bb"3053 3093 306b 3061 306f")
    testEncode(cb"Добрый день")(bb"0414 043e 0431 0440 044b 0439 0020 0434 0435 043d 044c")
    testEncode(cb"你好")(bb"4f60 597d")

    // 4-byte characters
    testEncode(cb"\ud835\udcd7\ud835\udcee\ud835\udcf5\ud835\udcf5\ud835\udcf8")(
      bb"d835 dcd7 d835 dcee d835 dcf5 d835 dcf5 d835 dcf8")

    testEncode(cb"")(bb"")

    // Here begin the sequences with at least one error

//    // Single UTF-16 surrogates
//    testEncode(cb"\ud800")(Malformed(1))
//    testEncode(cb"\udaff")(Malformed(1))
//    testEncode(cb"\udb80")(Malformed(1))
//    testEncode(cb"\udbff")(Malformed(1))
//    testEncode(cb"\udc00")(Malformed(1))
//    testEncode(cb"\udf80")(Malformed(1))
//    testEncode(cb"\udfff")(Malformed(1))
//
//    // High UTF-16 surrogates not followed by low surrogates
//    testEncode(cb"\ud800A")(Malformed(1), bb"0041")
//    testEncode(cb"\ud800\ud800")(Malformed(1), Malformed(1))
//    testEncode(cb"\ud800\ud835\udcd7")(Malformed(1), bb"d835 dcd7")
//    testEncode(cb"\udbffA")(Malformed(1), bb"0041")
//    testEncode(cb"\udbff\udb8f")(Malformed(1), Malformed(1))
//    testEncode(cb"\udbff\ud835\udcd7")(Malformed(1), bb"d835 dcd7")
  }
}
