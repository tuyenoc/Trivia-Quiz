package com.tuyennguyen.trivia_quiz.repositories

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.tuyennguyen.trivia_quiz.entities.User
import com.tuyennguyen.trivia_quiz.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class UserRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) {

    companion object {
        val TAG = UserRepository::class.simpleName
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            firebaseAuth
                .signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthResult? {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val user = hashMapOf(
                "avatar" to "",
                "fullName" to email.split("@")[0],
                "score" to 0,
            )

            firestore.collection("users")
                .document(getUserUid()!!)
                .set(user)
                .await()
            return authResult

        } catch (e: Exception) {
            null
        }
    }

    fun getUserUid(): String? {
        return if (firebaseAuth.currentUser == null) {
            null
        } else {
            firebaseAuth.currentUser!!.uid
        }
    }

    suspend fun getUser(userUid: String): Flow<Resource<User>> = callbackFlow {
        val userDocument = firestore
            .collection("users")
            .document(userUid)

        val subscription = userDocument.addSnapshotListener { snapshot, _ ->
            when {
                snapshot == null -> {
                    trySend(Resource.error(null, "")).isSuccess
                }
                snapshot.exists() -> {
                    snapshot.data?.let {
                        val fullName: String = it["fullName"] as String
                        val avatar: String = it["avatar"] as String
                        val score: Long = it["score"] as Long
                        trySend(Resource.success(User(fullName, avatar, score))).isSuccess
                    }
                }
                else -> {
                    trySend(Resource.error(null, "")).isSuccess
                }
            }
        }
        awaitClose { subscription.remove() }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun updateProfile(avatar: Bitmap, fullName: String, score: Long): Boolean {
        try {
            val storageRef = firebaseStorage.reference.child("avatars/${getUserUid()!!}")
            val byteArrayOutputStream = ByteArrayOutputStream()
            avatar.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val avatarBytes = byteArrayOutputStream.toByteArray()

            storageRef.putBytes(avatarBytes).await()
            val uriAvatar = storageRef.downloadUrl.await()

            val user = hashMapOf(
                "avatar" to uriAvatar.toString(),
                "fullName" to fullName,
                "score" to score,
            )

            firestore.collection("users")
                .document(getUserUid()!!)
                .set(user, SetOptions.merge())
                .await()

            return true

        } catch (e: Exception) {
            Log.d(TAG, "updateProfile: Exception" + e.message)
            return false
        }
    }


}