package com.sohail.patternizer.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Table;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by SOHAIL on 05/10/16.
 */

@ContentProvider(authority = Patternizer_provider.AUTHORITY,database = Patternizer_provider.PatternDatabase.class,packageName = "com.sohail.providerpackage")
public class Patternizer_provider {
    public static final String AUTHORITY = "com.sohail.patternizer";

    interface Path {
        String PATTERNS = "patterns";

    }
    @TableEndpoint(table = PatternDatabase.PATTERNS)
    public static class Patterns {

        @ContentUri(
                path = "patterns",
                type = "vnd.android.cursor.dir/list",
                defaultSort = PatternColumns._ID + " ASC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/patterns");

    }

    public interface PatternColumns {

        @DataType(DataType.Type.INTEGER)
        @PrimaryKey
        @AutoIncrement
        String _ID = "_id";

        @DataType(DataType.Type.INTEGER)
        String PATTERN_ID = "pattern_id";

        @DataType(DataType.Type.TEXT)
        String TITLE = "title";

        @DataType(DataType.Type.TEXT)
        String USER_NAME = "user_name";

        @DataType(DataType.Type.INTEGER)
        String LIKES = "likes";

        @DataType(DataType.Type.INTEGER)
        String VIEWS = "views";

        @DataType(DataType.Type.TEXT)
        String IMAGE_URL = "image_url";

        @DataType(DataType.Type.TEXT)
        String API_URL = "api_url";
    }

    @Database(version = PatternDatabase.VERSION,packageName = "com.sohail.providerpackage")
    public final class PatternDatabase {

        public static final int VERSION = 7;

        @Table(PatternColumns.class)
        public static final String PATTERNS = "patterns";
    }

    @NotifyBulkInsert(paths = Path.PATTERNS)
    public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
        return new Uri[]{
                uri,
        };
    }

}
