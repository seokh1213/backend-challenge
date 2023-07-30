package forever.backend.api.controller

import forever.backend.api.model.response.BaseResponse
import forever.backend.api.service.ReadService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/read")
class ReadController(
    private val readService: ReadService
) {

    @PostMapping
    fun readAction(
        @RequestParam("article_id") articleId: Long,
        @RequestParam("user_id") userId: Long
    ): BaseResponse<Boolean> {
        readService.sendReadAction(articleId, userId)
        return BaseResponse(true)
    }

}
