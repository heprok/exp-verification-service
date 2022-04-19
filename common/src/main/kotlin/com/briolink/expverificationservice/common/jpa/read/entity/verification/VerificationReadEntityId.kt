// package com.briolink.expverificationservice.common.jpa.read.entity.verification
//
// import com.briolink.expverificationservice.common.enumeration.ObjectConfirmTypeEnum
// import com.briolink.expverificationservice.common.types.ObjectConfirmId
// import org.hibernate.annotations.Type
// import java.io.Serializable
// import java.util.UUID
// import javax.persistence.Column
// import javax.persistence.MappedSuperclass
//
// @MappedSuperclass
// abstract class VerificationReadEntityId(id: ObjectConfirmId) : Serializable {
//
//     @Column(name = "object_confirm_type", nullable = false)
//     var _objectConfirmType: Int = id.typeKey
//
//     @Type(type = "pg-uuid")
//     @Column(name = "object_confirm_id", nullable = false, length = 36)
//     var objectConfirmId: UUID = id.id
//
//     var objectType: ObjectConfirmTypeEnum
//         get() = ObjectConfirmTypeEnum.fromInt(_objectConfirmType)
//         set(value) {
//             _objectConfirmType = value.value
//         }
//
//     override fun equals(other: Any?): Boolean {
//         if (this === other) return true
//         if (other !is VerificationReadEntityId) return false
//
//         if (_objectConfirmType != other._objectConfirmType) return false
//         if (objectConfirmId != other.objectConfirmId) return false
//
//         return true
//     }
//
//     override fun hashCode(): Int {
//         var result = _objectConfirmType
//         result = 31 * result + objectConfirmId.hashCode()
//         return result
//     }
// }
