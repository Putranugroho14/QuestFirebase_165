package com.example.firebase.repositori

import com.example.firebase.modeldata.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
}

class FirebaseRepositorySiswa : RepositorySiswa {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            collection.get().await().documents.map { doc ->
                Siswa(
                    id = doc.getLong("id")?.toLong() ?: 0L,
                    nama = doc.getString("Nama") ?: "",
                    alamat = doc.getString("Alamat") ?: "",
                    telpon = doc.getString("Telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun postDataSiswa(siswa: Siswa) {
        val docRef = if (siswa.id == 0L) {
            collection.document()
        } else {
            collection.document(siswa.id.toString())
        }

        val data = hashMapOf(
            "id" to (siswa.id.takeIf { it != 0L } ?: docRef.id.hashCode().toLong()),
            "Nama" to siswa.nama,
            "Alamat" to siswa.alamat,
            "Telpon" to siswa.telpon
        )

        docRef.set(data).await()
    }
}