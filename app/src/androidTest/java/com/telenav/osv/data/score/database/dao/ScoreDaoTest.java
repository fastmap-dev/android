package com.telenav.osv.data.score.database.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Context;
import com.telenav.osv.data.DataTestHelper;
import com.telenav.osv.data.database.OSCDatabase;
import com.telenav.osv.data.score.database.entity.ScoreEntity;
import com.telenav.osv.data.sequence.database.dao.SequenceDao;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author horatiuf
 */
@RunWith(AndroidJUnit4.class)
public class ScoreDaoTest {

    private SequenceDao sequenceDao;

    private ScoreDao scoreDao;

    private OSCDatabase database;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, OSCDatabase.class).build();
        scoreDao = database.scoreDao();
        sequenceDao = database.sequenceDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insert() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);

        ScoreEntity dbScoreEntity = scoreDao.findByID(scoreEntity.getScoreId()).blockingGet();

        DataTestHelper.assertScoresDataEntity(scoreEntity, dbScoreEntity);
    }

    @Test
    public void update() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);
        ScoreEntity dbScoreEntity = scoreDao.findByID(scoreEntity.getScoreId()).blockingGet();
        DataTestHelper.assertScoresDataEntity(scoreEntity, dbScoreEntity);
    }

    @Test
    public void delete() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);

        int rowNo = scoreDao.deleteById(scoreEntity.getScoreId());

        assertEquals(rowNo, 1);
    }

    @Test
    public void findAll() {
        Map<String, ScoreEntity> scoreEntityMap = DataTestHelper.assertInsertGetScoresMap(3, 10, scoreDao, sequenceDao);

        List<ScoreEntity> dbScoreEntities = scoreDao.findAll().blockingGet();

        DataTestHelper.assertScoreEntitiesList(scoreEntityMap, dbScoreEntities);
    }

    @Test
    public void findAllByIds() {
        Map<String, ScoreEntity> scoreEntityMap = DataTestHelper.assertInsertGetScoresMap(3, 10, scoreDao, sequenceDao);
        DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 5);
        DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 6);
        DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 8);
        DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 9);

        List<ScoreEntity> dbScoreEntities = scoreDao.findAllByIds(scoreEntityMap.keySet().toArray(new String[0])).blockingGet();

        DataTestHelper.assertScoreEntitiesList(scoreEntityMap, dbScoreEntities);
    }

    @Test
    public void findAllBySequenceID() {
        int scorePerSequence = 10;
        Map<String, ScoreEntity> scoreEntityMap = DataTestHelper.assertInsertGetScoresMap(1, scorePerSequence, scoreDao, sequenceDao);
        DataTestHelper.assertInsertGetScoresMap(1, 7, scoreDao, sequenceDao);
        DataTestHelper.assertInsertGetScoresMap(1, 9, scoreDao, sequenceDao);

        List<ScoreEntity> dbScoreEntities = scoreDao.findAllBySequenceID(new ArrayList<>(scoreEntityMap.values()).get(0).getSequenceID()).blockingGet();

        assertEquals(dbScoreEntities.size(), scorePerSequence);
        DataTestHelper.assertScoreEntitiesList(scoreEntityMap, dbScoreEntities);
    }

    @Test
    public void findByID() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);

        ScoreEntity dbScoreEntity = scoreDao.findByID(scoreEntity.getScoreId()).blockingGet();

        DataTestHelper.assertScoresDataEntity(scoreEntity, dbScoreEntity);
    }

    @Test
    public void findBySequenceID() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);
        ScoreEntity dbScoreEntity = scoreDao.findBySequenceID(scoreEntity.getSequenceID()).blockingGet();
        DataTestHelper.assertScoresDataEntity(scoreEntity, dbScoreEntity);

    }

    @Test
    public void updateObdPhotoCount() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);

        int obdFrameCount = new Random().nextInt();
        int updateNo = scoreDao.updateObdPhotoCount(scoreEntity.getScoreId(), obdFrameCount);
        ScoreEntity dbScoreEntity = scoreDao.findByID(scoreEntity.getScoreId()).blockingGet();

        assertEquals(updateNo, 1);
        assertEquals(obdFrameCount, (int) dbScoreEntity.getObdFrameCount());
        assertNotEquals(scoreEntity.getObdFrameCount(), dbScoreEntity.getObdFrameCount());
    }

    @Test
    public void updatePhotoCount() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);

        int frameCount = new Random().nextInt();
        int updateNo = scoreDao.updatePhotoCount(scoreEntity.getScoreId(), frameCount);
        ScoreEntity dbScoreEntity = scoreDao.findByID(scoreEntity.getScoreId()).blockingGet();

        assertEquals(updateNo, 1);
        assertEquals(frameCount, (int) dbScoreEntity.getFrameCount());
        assertNotEquals(scoreEntity.getFrameCount(), dbScoreEntity.getFrameCount());
    }

    @Test
    public void deleteByScoreId() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);
        int deleteNo = scoreDao.deleteById(scoreEntity.getScoreId());
        assertEquals(deleteNo, 1);
    }

    @Test
    public void deleteBySequenceId() {
        ScoreEntity scoreEntity = DataTestHelper.assertInsertGetAutogeneratedSequenceScore(sequenceDao, scoreDao, 3);
        int deleteNo = scoreDao.deleteBySequenceId(scoreEntity.getSequenceID());
        assertEquals(deleteNo, 1);
    }

    @Test
    public void deleteAll() {
        int noOfScore = 8;
        int noOfSequence = 8;

        DataTestHelper.assertInsertGetScoresMap(noOfSequence, noOfScore, scoreDao, sequenceDao);

        int deleteAll = scoreDao.deleteAll();

        assertEquals(deleteAll, noOfScore * noOfSequence);
    }
}