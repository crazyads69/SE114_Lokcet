package com.grouptwo.lokcet.view.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.grouptwo.lokcet.R
import com.grouptwo.lokcet.data.model.Message
import com.grouptwo.lokcet.data.model.User
import com.grouptwo.lokcet.utils.toCustomDateFormat
import com.grouptwo.lokcet.utils.toCustomTimeFormat
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MessageItem(
    message: Message,
    shouldShowFriendAvatar: Boolean,
    currentUser: User,
    isShowTimeStamp: Boolean,
    friendMap: Map<String, User>
) {
    val messageReceived = currentUser.id != message.senderId
    val backgroundColor = if (messageReceived) Color(0xE37E7878) else Color.White
    val alignment = if (messageReceived) Alignment.Start else Alignment.End
    val shape = if (messageReceived) {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 20.dp, bottomEnd = 10.dp)
    } else {
        RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 8.dp, bottomEnd = 20.dp)
    }
    val textColor = if (messageReceived) Color.White else Color.Black

    // Show reply feed if message has feed image
    if (message.isReplyToFeed) {
        // Show reply feed message
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            if (isShowTimeStamp) {
                Text(
                    text = message.createdAt.toCustomTimeFormat(),
                    color = Color(0xFF7A7878),
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the timestamp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Show the reply feed image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(20))
                    .aspectRatio(1f)
                    .padding(12.dp)
            ) {
                GlideImage(
                    imageModel = { message.feed.imageUrl },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(20)
                        )
                        .aspectRatio(1f)
                        .align(Alignment.Center),
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop, alignment = Alignment.Center
                    ),
                    requestOptions = {
                        RequestOptions().diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        ).centerCrop()
                    },
                )
                // Show info of the feed
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(24.dp)
                        .background(
                            Color(0xFF272626).copy(alpha = 0.5f), shape = RoundedCornerShape(50)
                        )
                ) {
                    // Show user avatar and name
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        // Show user avatar
                        GlideImage(
                            imageModel = { friendMap[message.feed.userId]?.profilePicture },
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Crop, alignment = Alignment.Center
                            ),
                            requestOptions = {
                                RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                            },
                            loading = {
                                // Show a circular progress indicator when loading.
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp), color = Color(0xFFE5A500)
                                )
                            },
                            failure = {
                                // Show a circular progress indicator when loading.
                                Image(
                                    painter = painterResource(id = R.drawable.icon_friend),
                                    contentDescription = "Friend Icon",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier
                                .size(18.dp)
                                .clip(shape = CircleShape)
                                .border(
                                    width = 1.dp, color = Color(0xFFE5A500), shape = CircleShape
                                )
                        )
                        // Show user name
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentUser.id == message.feed.userId) "Bạn" else "${friendMap[message.feed.userId]?.firstName}",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
                // Show time of the feed
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.TopEnd)
                        .padding(24.dp)
                        .background(
                            Color(0xFF272626).copy(alpha = 0.5f), shape = RoundedCornerShape(50)
                        )
                ) {
                    Text(
                        text = message.feed.createdAt.toCustomDateFormat(),
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }

            }
            // Show message item
            // Check if friend avatar should be shown
            if (shouldShowFriendAvatar) {
                // Show friend message item with friend avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Show friend avatar
                    GlideImage(
                        imageModel = { friendMap[message.senderId]?.profilePicture },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop, alignment = Alignment.Center
                        ),
                        requestOptions = {
                            RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                        },
                        loading = {
                            // Show a circular progress indicator when loading.
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp), color = Color(0xFFE5A500)
                            )
                        },
                        failure = {
                            // Show a circular progress indicator when loading.
                            Image(
                                painter = painterResource(id = R.drawable.icon_friend),
                                contentDescription = "Friend Icon",
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .padding(4.dp)
                            .clip(shape = CircleShape)
                            .border(
                                width = 1.dp, color = Color(0xFFE5A500), shape = CircleShape
                            )
                    )
                    // Show message item
                    Text(
                        text = message.messageContent,
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(
                                color = backgroundColor, shape = shape
                            )
                            .padding(8.dp)
                    )
                }
            } else {
                if (currentUser.id != message.senderId) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Friend message without avatar
                        Spacer(modifier = Modifier.width(34.dp))// Add space between avatar and message
                        // Show message item
                        Text(
                            text = message.messageContent,
                            color = textColor,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .background(
                                    color = backgroundColor, shape = shape
                                )
                                .padding(8.dp)
                        )
                    }
                }
                // Show message item
                else {
                    // Current user message
                    Text(
                        text = message.messageContent,
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(
                                color = backgroundColor, shape = shape
                            )
                            .align(alignment)
                            .padding(8.dp)
                    )
                }
            }
        }
    } else {
        // Show normal message
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            if (isShowTimeStamp) {
                Text(
                    text = message.createdAt.toCustomTimeFormat(),
                    color = Color(0xFF7A7878),
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the timestamp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Check if friend avatar should be shown
            if (shouldShowFriendAvatar) {
                // Show friend message item with friend avatar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Show friend avatar
                    GlideImage(
                        imageModel = { friendMap[message.senderId]?.profilePicture },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop, alignment = Alignment.Center
                        ),
                        requestOptions = {
                            RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                        },
                        loading = {
                            // Show a circular progress indicator when loading.
                            CircularProgressIndicator(
                                modifier = Modifier.size(30.dp), color = Color(0xFFE5A500)
                            )
                        },
                        failure = {
                            // Show a circular progress indicator when loading.
                            Image(
                                painter = painterResource(id = R.drawable.icon_friend),
                                contentDescription = "Friend Icon",
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .padding(4.dp)
                            .clip(shape = CircleShape)
                            .border(
                                width = 1.dp, color = Color(0xFFE5A500), shape = CircleShape
                            )
                    )
                    // Show message item
                    Text(
                        text = message.messageContent,
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(
                                color = backgroundColor, shape = shape
                            )
                            .padding(8.dp)
                    )
                }
            } else {
                if (currentUser.id != message.senderId) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Friend message without avatar
                        Spacer(modifier = Modifier.width(34.dp))// Add space between avatar and message
                        // Show message item
                        Text(
                            text = message.messageContent,
                            color = textColor,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .background(
                                    color = backgroundColor, shape = shape
                                )
                                .padding(8.dp)
                        )
                    }
                }
                // Show message item
                else {
                    // Current user message
                    Text(
                        text = message.messageContent,
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .background(
                                color = backgroundColor, shape = shape
                            )
                            .align(alignment)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}