package forever.backend.api.model.response

data class BaseResponse<T>(
    val data: T? = null,
    val message: String = "성공",
    val status: Int = 0
)
