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
    private val cipherJsonEnc = "{\"id\":\"aa770bed-e199-41f1-90b1-c4578104e22b\",\"favorite\":false,\"dir\":\"622e5baf-f4b4-427b-b1dd-d54cded668e3\",\"data\":\"cc52ce36d8e1d47aada7dbc4735852437f43718f05ba23eac2e111ec82a32bfe6b5e300d8f74a758dec89fcd77a30cefff4ab82477a8e800274c93800b53751925f8bf02522682fe313574d79eb38bfab691c207c825cc192c2e2869ccd5a1b1ef457e7a394b30ebfd6c486dd87cb8a203aa630365de4368a71e2cfd1a61f11a67f82b842dd15266d49a7314c46c578807b890b3415f2271ee4751e778603463c4f9fa0b442f16774684c93c8981286997b52df3c5fa8f1b5685e86cb7254a926d94d85503d40a6b0fbcd226f9c666dcab4c683f46a47fb73ef7e1374f42e27d37831b243b80c7e4c70536b93f1af9aaa38550c99aef03216af8990c567786c2c4f0c7d23b87eadea9a7f5e7040176e0645be16e94ea8e6b7df14f1275d8bea92c6664adfcd549954d84bc7fadbf496fbbcd7e9ad6d05556dd530e0483ec176f963722064df3c189ed766b960d8b4bfce2170536c393dbbf2b706a4bca712ec27157b218a485cd1ff449bb5b85acda659f0f0c057ea4bda5006c2e9afca51c5758e3b341731616e40a6357a4fa7c499f464ca8aa48b5ddf58e9f7ec84d0030374290f4f486a40c3d84339fcc90cc32a3a7e85b109ad9f02a4620a6e24bd2c93c9a31fda2077160b2e888ef92c28a34a4d9b9f3b71e539184869acf98cada0590074c9456536f5b0299dc6ea946ffeafe54102caff6b7f27208552401436b1ab3d235673722f6fbb3319caf80dd429625\"}"
    private val encryptionKey = Pbkdf2(1000).sha256("hello world", "salt".toByteArray())

    @Test
    fun deserialization() {
        val a = Cipher.of(cipherJson)
        val b = Cipher.of(cipherJsonEnc, encryptionKey)

        assertEquals(a, b)

        val encCipher = EncryptedCipher.of(a, encryptionKey)
        Cipher.of(encCipher, encryptionKey)
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
