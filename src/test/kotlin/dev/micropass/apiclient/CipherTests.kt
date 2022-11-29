package dev.micropass.apiclient

import dev.medzik.libcrypto.Pbkdf2
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CipherTests {
    private val cipherJson = """
{
    "id": "aa770bed-e199-41f1-90b1-c4578104e22b",
    "favorite": false,
    "dir": "622e5baf-f4b4-427b-b1dd-d54cded668e3", // maybe null
    "data": {
        "type": 1,
        "name": "Example",
        "favorite": true,
        "fields": {
            "user": {
                "typ": -1, // -1 for default field (built-in email field)
                "val": "medzik@example.com"
            }, // maybe null
            "pass": {
                "typ": -1, // -1 for default field (built-in password field)
                "val": "SecretPassword"
            }, // maybe null
            "totp": {
                "typ": -1, // -1 for default field (built-in OTP field)
                "val": "otpauth://totp/medzik@example.com?secret=JBSWY3DPEHPK3PXP&issuer=example.com"
            }, // maybe null
            "url": {
                "typ": -1, // -1 for default field (built-in URL field)
                "val": "https://example.com"
            }, // maybe null
            "note": {
                "typ": -1, // -1 for default field (built-in note field)
                "val": "my note about this cipher"
            }, // maybe null
            "Custom Field": {
                "typ": 0, // 0 for text field
                "val": "This is a text in your custom field"
            }, // custom field
            "Custom Secret Field": {
                "typ": 1, // 1 for hidden text field
                "val": "This is a secret text in your secret custom field"
            } // custom field
        }
    },
    "attachments": [] // not implemented yet
}
"""
    private val cipherJsonEnc = "32e8ee1bec3e4276c32416c7c1259030813f54f223dc0c943ccf49667b5a4ca27ae61fe879a915e540f842c216b6d0e564ae0b4b74de1a191ffc2c89117143b16b9ca721a814a575de4ffedc0afc278fd124b7e8abee5afd2800305ecdd03b2171631151341be4e97d6bb1fdeb5199de78b2e713b8d17c7360846482a35f1db47578b7113767461aacbf851b2f57388b86064c44cedba1b97f2efc5fed237d1f843923fe1dde63466c6ce222966ec66cfaecc31bb4fd51fae099ed2d3a62d77eeaccd7f3fd90d48d38f52ece0f0af53dae0c6aefafa590188fc9b91ce807e8e56497355dbfec0276163f422f2825f34692738b40aa2040d86b7ae6ba0fd11015183b1593b99f5dee44c6f3a54343027d0d0a0b1f522c49638745196eb4ecab2f0622618784c7ebcff15d194c35da6fc5b79321f5d4b7a49c394429294f4e78234ed8baf6199d593a68fd9b53f7e442858d8cb32e6238f1ef74c4eca263390dde3ac5c579e3d125e24ce7956540f69203fa452877413f99499ec8d0c1861f30503bf5305ea6cce5d4b2bf839915756778a76f6127c92e9f9a0991ae1ea1426805567b81f4f2f67c43898fcd7a9979437dc4b06845fa922ddf01b1a5fe4eea52c4b38e35942b89c39a5f725c2614a139a254201b7f08a0709c29808741d5409db4ab4a59951cc11a4e822064f14c4d0f1310b1c587c7541f61dd2b71ec7dc23c07af6e67563e62eafcd878afdafbd4ef3389ab56ed0b1e16008ad4dfe4a3c2b47982acd3ad759115698e2407df7bc7cffb"
    private val encryptionKey = Pbkdf2(1000).sha256("hello world", "salt".toByteArray())

    @Test
    fun deserialization() {
        val a = Cipher.of(cipherJson)
        val b = Cipher.of(cipherJsonEnc, encryptionKey)

        assertEquals(a, b)
    }

    @Test
    fun serialization() {
        val cipher = Cipher.of(cipherJson)

        // test json serialization
        val cipherJson = cipher.toJson()
        val a = Cipher.of(cipherJson)

        val cipherJsonEnc = cipher.toJson(encryptionKey)
        val b = Cipher.of(cipherJsonEnc, encryptionKey)

        assertEquals(a, cipher)
        assertEquals(b, cipher)
    }
}
