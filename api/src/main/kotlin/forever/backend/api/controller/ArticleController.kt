package forever.backend.api.controller

import forever.backend.api.model.request.CreateArticleRequest
import forever.backend.api.model.response.BaseResponse
import forever.backend.api.service.ArticleService
import forever.backend.mysql.entity.content.Article
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/article")
class ArticleController(
    private val articleService: ArticleService
) {

    @PostMapping
    fun createArticle(@RequestBody createArticleRequest: CreateArticleRequest): BaseResponse<Article> {
        return BaseResponse(articleService.createArticle(createArticleRequest))
    }

    @RequestMapping
    fun test(): BaseResponse<String> {
        articleService.test()
        return BaseResponse("test")
    }
}
