const express = require('express')
const history = require('connect-history-api-fallback')
const dotenv = require('dotenv')
const morgan = require('morgan')
const createError = require('http-errors')
const cookieParser = require('cookie-parser');
const cors = require('cors');
const { default: axios } = require('axios');

dotenv.config()

const app = express()
app.use(morgan( process.env.ENVIRONMENT === 'production'? 'combined':'dev'))

app.get('/healthcheck', (req, res, next) => res.status(200).send('success'))

app.use(cors({
	origin:'*',
	credentials:true,
}))

app.use(express.json())
app.use(express.urlencoded( {extended : false } ))
app.use(cookieParser(process.env.SECRET_KEY))

app.use(history())

app.use(express.static('dist'))

/* ERROR HANDLER */
app.use(function (req, res, next) {next(createError(404))}) //일치하는 라우터가 없을 경우 404
app.use(function (err, req, res, next) { //ERROR HANDLER
	if(err.message.includes('connect')) return res.status(500).json({msg:'서버와 연결할 수 없습니다.'})
	switch(err.status) {
		case 400: return res.status(400).json(err.message?{msg:err.message}:createError(400)) // 잘못된 요청
		case 401: return res.status(401).json(err.message?{msg:err.message}:createError(401)) // Unauthenticated, 권한 없음
		case 403: return res.status(403).json(err.message?{msg:err.message}:createError(403)) // Forbidden, 금지됨
		case 404: return res.status(404).json(err.message?{msg:err.message}:createError(404)) // Not Found, 찾을 수 없음
		case 422: return res.status(422).json(err.message?{msg:err.message}:createError(422)) // 처리할 수 없는 엔티티
		case 500: return res.status(500).json(err.message?{msg:err.message}:createError(500)) // 내부 서버 오류, 외부 API 오류
		case 502: return res.status(502).json(err.message?{msg:err.message}:createError(502)) // Bad Gateway, 업스트림 서버에서 잘못된 응답을 받음
		default : return res.status(500).json(err.message?{msg:err.message}:createError(500)) // 내부 서버 오류
	}
})

app.listen(process.env.PORT | 3000,()=>{
	console.log(`server port:${process.env.PORT}`)
})